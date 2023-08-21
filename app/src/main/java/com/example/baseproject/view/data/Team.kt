package com.example.baseproject.view.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class to represent team for the view layer
 */
@Parcelize
data class Team(
    val id: String,
    val name: String,
    val totalWins: Int,
    val totalLosses: Int,
    val totalDraws: Int,
    val winsPer: Float,
    val details: List<TeamDetail>
) : Parcelable