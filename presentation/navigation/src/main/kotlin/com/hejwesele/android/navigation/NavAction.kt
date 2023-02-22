package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.spec.Direction

data class NavAction(
    val route: Direction,
    val options: NavOptionsBuilder.() -> Unit = {  }
)
