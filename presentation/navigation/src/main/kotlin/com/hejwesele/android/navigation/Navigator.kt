package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.spec.Direction

interface Navigator {

    suspend fun navigate(direction: Direction, builder: NavOptionsBuilder.() -> Unit = {})
}
