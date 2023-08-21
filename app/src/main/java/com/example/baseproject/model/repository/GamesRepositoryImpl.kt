package com.example.baseproject.model.repository

import android.util.Log
import com.example.baseproject.mapper.toGameEntityList
import com.example.baseproject.model.local.GamesLocalDataSource
import com.example.baseproject.model.local.data.GameEntity
import com.example.baseproject.model.remote.GamesRemoteDataSource
import com.example.baseproject.view.data.Team
import com.example.baseproject.view.data.TeamDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Repository Impl to get data either from local data source and remote data source.
 * Repository maintains single source of truth abstraction.
 */
class GamesRepositoryImpl @Inject constructor(
    private val localDataSource: GamesLocalDataSource,
    private val remoteDataSource: GamesRemoteDataSource
) : GamesRepository {

    private val teamsRepoMap = mutableMapOf<String, TeamRepo>()


    companion object {
        private const val TAG = "GamesRepositoryImpl"
    }

    override suspend fun fetchTeamsList(): Flow<Result<List<Team>>> = flow<Result<List<Team>>> {
        //Fetch data from database
        val gamesLocalFlow = localDataSource.getAllGames()

        gamesLocalFlow.collect { state ->
            if (state.isEmpty()) {
                //If the list from DB is empty, make a network call - get data, store it in DB.
                val result = remoteDataSource.fetchGames()
                if (result.isSuccess) {
                    Log.d(TAG, "fetchAllGames: from API ${result.getOrNull()}")
                    result.getOrNull()?.let { gamesResponseList ->
                        val gameEntityList = gamesResponseList.toGameEntityList()
                        localDataSource.insertAllGames(gameEntityList)
                        val teamsList = convertToTeamList(gameEntityList)
                        emit(Result.success(teamsList))
                    } ?: emit(Result.failure(Exception("Empty List")))
                } else {
                    result.exceptionOrNull()?.let { emit(Result.failure(it)) }
                }
            } else {
                gamesLocalFlow.collect { gameEntityList ->
                    Log.d(TAG, "fetchAllGames: from database $gameEntityList")
                    val teamsList = convertToTeamList(gameEntityList)
                    emit(Result.success(teamsList))
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun convertToTeamList(gameEntityList: List<GameEntity>): MutableList<Team> {
        addToTeamsMapWithScores(gameEntityList)
        calculateWinsPercentage()
        val teamsList = mutableListOf<Team>()
        teamsRepoMap.values.map { teamsList.add(it.toTeam()) }
        return teamsList
    }

    private fun calculateWinsPercentage() {
        teamsRepoMap.forEach { (_, teamRepo) ->
            teamRepo.winsPer = getWinPercentage(teamRepo)
        }
    }

    private fun addToTeamsMapWithScores(gameEntityList: List<GameEntity>) {
        gameEntityList.forEach { gameEntity ->
            calculateTeamScores(gameEntity)
        }
    }

    private fun calculateTeamScores(
        gameEntity: GameEntity
    ) {
        val awayTeam = getAwayTeam(gameEntity)
        val homeTeam = getHomeTeam(gameEntity)
        val awayTeamDetail =
            awayTeam.detailsMap.getOrDefault(homeTeam.id, TeamDetailRepo(homeTeam.id, homeTeam.name))
        val homeTeamDetail =
            homeTeam.detailsMap.getOrDefault(awayTeam.id, TeamDetailRepo(awayTeam.id, awayTeam.name))
        if (gameEntity.awayScore > gameEntity.homeScore) {
            awayTeam.totalWins++
            awayTeamDetail.losses++
            awayTeamDetail.total++

            homeTeam.totalLosses++
            homeTeamDetail.wins++
            homeTeamDetail.total++
        } else if (gameEntity.awayScore < gameEntity.homeScore) {
            awayTeam.totalLosses++
            awayTeamDetail.wins++
            awayTeamDetail.total++

            homeTeam.totalWins++
            homeTeamDetail.losses++
            homeTeamDetail.total++
        } else {
            awayTeam.totalDraws++
            awayTeamDetail.draws++
            awayTeamDetail.total++

            homeTeam.totalDraws++
            homeTeamDetail.draws++
            homeTeamDetail.total++
        }
        awayTeam.detailsMap[homeTeam.id] = awayTeamDetail
        homeTeam.detailsMap[awayTeam.id] = homeTeamDetail

        teamsRepoMap[awayTeam.id] = awayTeam
        teamsRepoMap[homeTeam.id] = homeTeam
    }

    private fun getHomeTeam(gameEntity: GameEntity): TeamRepo =
        teamsRepoMap.getOrDefault(
            gameEntity.homeTeamId,
            TeamRepo(id = gameEntity.homeTeamId, name = gameEntity.homeTeamName)
        )


    private fun getAwayTeam(gameEntity: GameEntity): TeamRepo =
        teamsRepoMap.getOrDefault(
            gameEntity.awayTeamId,
            TeamRepo(id = gameEntity.awayTeamId, name = gameEntity.awayTeamName)
        )

    private fun getWinPercentage(team: TeamRepo): Float {
        val total =
            ((team.totalWins).toFloat() / (team.totalWins + team.totalLosses + team.totalDraws).toFloat()) * 100
        return roundToTwoDecimalPlaces(total)
    }

    private fun roundToTwoDecimalPlaces(value: Float): Float {
        val decimalFormat = DecimalFormat("#.00")
        return decimalFormat.format(value).toFloat()
    }

    // Helper class to build team information from game entities
    private class TeamRepo(
        val id: String,
        val name: String
    ) {
        var totalWins: Int = 0
        var totalLosses: Int = 0
        var totalDraws: Int = 0
        var winsPer: Float = 0.0F
        var detailsMap: MutableMap<String, TeamDetailRepo> = mutableMapOf()
    }

    // Helper class to build team details information from game entities
    private class TeamDetailRepo(
        val id: String,
        var name: String
    ) {
        var wins: Int = 0
        var losses: Int = 0
        var draws: Int = 0
        var total: Int = 0
    }

    private fun TeamRepo.toTeam() = Team(id, name, totalWins, totalLosses, totalDraws, winsPer, detailsMap.toTeamDetailsList())

    private fun MutableMap<String, TeamDetailRepo>.toTeamDetailsList(): List<TeamDetail> {
        return this.values.map { it.toTeamDetail() }
    }

    private fun TeamDetailRepo.toTeamDetail() = TeamDetail(id, name, wins, losses, draws, total)
}