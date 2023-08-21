package com.example.baseproject.model.remote.data

import com.google.gson.annotations.SerializedName

/**
 * Data class to represent game from the remote data source layer
 */
data class GameResponse(
    @SerializedName("gameId")
    val gameId: String,
    @SerializedName("awayTeamId")
    val awayTeamId: String,
    @SerializedName("awayTeamName")
    val awayTeamName: String,
    @SerializedName("awayScore")
    val awayScore: Int,
    @SerializedName("homeTeamId")
    val homeTeamId: String,
    @SerializedName("homeTeamName")
    val homeTeamName: String,
    @SerializedName("homeScore")
    val homeScore: Int
)