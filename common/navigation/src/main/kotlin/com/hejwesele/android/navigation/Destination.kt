package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder

data class Destination(
    val route: String,
    val options: NavOptionsBuilder.() -> Unit = {}
)
