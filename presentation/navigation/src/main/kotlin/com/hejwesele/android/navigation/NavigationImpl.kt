package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.Route
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NavigationImpl @Inject constructor() : Navigator, Navigation {

    private val _navActions = MutableSharedFlow<NavAction>()
    override val navActions: SharedFlow<NavAction> = _navActions.asSharedFlow()

    override suspend fun navigate(direction: Direction, builder: NavOptionsBuilder.() -> Unit) {
        _navActions.emit(NavAction.To(direction, navOptions(builder)))
    }

    override suspend fun popBackStack(route: Route, inclusive: Boolean, saveState: Boolean) {
        _navActions.emit(NavAction.Pop(route, inclusive, saveState))
    }

    override suspend fun navigateUp() {
        _navActions.emit(NavAction.Up)
    }
}
