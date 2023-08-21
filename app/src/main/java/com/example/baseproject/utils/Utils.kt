package com.example.baseproject.utils

import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.example.baseproject.R

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction)
    }
}

fun TextView.setAscendingIcon() {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        0,
        0,
        R.drawable.sort_ascending,
        0
    )
}

fun TextView.setDescendingIcon() {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        0,
        0,
        R.drawable.sort_descending,
        0
    )
}