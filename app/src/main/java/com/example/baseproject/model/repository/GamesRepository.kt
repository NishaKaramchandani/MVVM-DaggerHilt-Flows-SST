package com.example.baseproject.model.repository

import com.example.baseproject.view.data.Team
import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    suspend fun fetchTeamsList(): Flow<Result<List<Team>>>
}