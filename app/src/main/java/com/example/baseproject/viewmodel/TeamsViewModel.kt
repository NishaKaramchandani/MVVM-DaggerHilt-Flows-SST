package com.example.baseproject.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.R
import com.example.baseproject.model.repository.GamesRepositoryImpl
import com.example.baseproject.view.data.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * View Model to fetch teams data
 */
@HiltViewModel
class TeamsViewModel @Inject constructor(private val repositoryImpl: GamesRepositoryImpl) :
    ViewModel() {
    private val _teamsUiState: MutableSharedFlow<TeamListUiState> = MutableSharedFlow(replay = 1)
    val teamsUiState: SharedFlow<TeamListUiState> get() = _teamsUiState.asSharedFlow()

    private var _teamsList: List<Team> = mutableListOf()

    companion object {
        private const val TAG = "ListViewModel"
    }

    init {
        // Calling data fetching logic in init so we don't fetch again when device is rotated.
        getAllTeams()
    }

    private fun getAllTeams() {
        viewModelScope.launch {
            Log.d(TAG, "getAllTeams: Fetching Data!!")
            _teamsUiState.emit(TeamListUiState.Loading)

            // Delay here to test loading state and simulate network latency.
            //delay(2000)

            repositoryImpl.fetchTeamsList().collect { value: Result<List<Team>> ->
                value.onSuccess { teamList ->
                    if (teamList.isNotEmpty()) {
                        _teamsList = teamList
                        _teamsUiState.emit(TeamListUiState.Success(teamsList = teamList.sortedByDescending { it.totalWins }))
                    } else {
                        _teamsUiState.emit(TeamListUiState.Error(R.string.error_fetching_countries_empty))
                    }
                }
                value.onFailure { error ->
                    val errorMessageId = when (error) {
                        is UnknownHostException -> R.string.offline_error_fetching
                        else -> R.string.error_fetching_countries
                    }
                    _teamsUiState.emit(TeamListUiState.Error(errorMessageId = errorMessageId))
                }
            }
        }
    }
}

enum class SORTORDER {
    ASCENDING, DESCENDING
}

sealed class TeamListUiState {
    data class Success(val teamsList: List<Team>) : TeamListUiState()
    data class Error(@StringRes val errorMessageId: Int) : TeamListUiState()
    data object Loading : TeamListUiState()
}