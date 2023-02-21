package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder

data class NavAction(
    val route: ComposeDirection,
    val options: NavOptionsBuilder.() -> Unit
)
