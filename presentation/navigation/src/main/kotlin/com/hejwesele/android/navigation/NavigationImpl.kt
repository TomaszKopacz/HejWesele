package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationImpl @Inject constructor() : Navigator, Navigation {

    private val _actions = MutableSharedFlow<NavAction>()
    override val actions: SharedFlow<NavAction> = _actions.asSharedFlow()

    override suspend fun navigate(direction: Direction, builder: NavOptionsBuilder.() -> Unit) {
        _actions.emit(NavAction(direction, builder))
    }
}
