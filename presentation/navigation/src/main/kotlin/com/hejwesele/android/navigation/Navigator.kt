package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.Route

interface Navigator {

    suspend fun navigate(direction: Direction, builder: NavOptionsBuilder.() -> Unit = {})
    suspend fun popBackStack(route: Route, inclusive: Boolean, saveState: Boolean = false)
    suspend fun navigateUp()
}
