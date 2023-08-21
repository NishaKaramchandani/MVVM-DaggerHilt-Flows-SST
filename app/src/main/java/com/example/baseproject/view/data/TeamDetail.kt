package com.example.baseproject.view.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class to represent team details for the view layer
 */
@Parcelize
data class TeamDetail(
    val id: String,
    val name: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val total: Int
) : Parcelable
