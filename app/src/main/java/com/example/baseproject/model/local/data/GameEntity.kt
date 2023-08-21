package com.example.baseproject.model.local.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class to represent game from the local data source layer
 */
@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val gameId: String,
    val homeTeamId: String,
    val homeTeamName: String,
    val homeScore: Int,
    val awayTeamId: String,
    val awayTeamName: String,
    val awayScore: Int
)