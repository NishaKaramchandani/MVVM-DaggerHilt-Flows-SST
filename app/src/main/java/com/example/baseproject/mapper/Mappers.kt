package com.example.baseproject.mapper

import com.example.baseproject.model.local.data.GameEntity
import com.example.baseproject.model.remote.data.GameResponse

/**
 * Contains different mappers to convert different layer game data objects into each other.
 */
fun GameResponse.toGameEntity() = GameEntity(
    gameId = gameId, homeTeamId = homeTeamId, homeScore = homeScore, homeTeamName = homeTeamName, awayTeamId = awayTeamId, awayScore = awayScore, awayTeamName = awayTeamName
)

fun List<GameResponse>.toGameEntityList(): List<GameEntity> {
    return this.map { it.toGameEntity() }
}


