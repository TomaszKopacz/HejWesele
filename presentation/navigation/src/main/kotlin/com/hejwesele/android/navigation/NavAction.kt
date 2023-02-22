package com.hejwesele.android.navigation

import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.Route

val EMPTY_OPTIONS = navOptions { }

sealed class NavAction {
    data class To(
        val direction: Direction,
        val options: NavOptions = EMPTY_OPTIONS
    ) : NavAction()

    data class Pop(
        val route: Route,
        val inclusive: Boolean,
        val saveState: Boolean = false
    ) : NavAction()

    object Up : NavAction()
}
