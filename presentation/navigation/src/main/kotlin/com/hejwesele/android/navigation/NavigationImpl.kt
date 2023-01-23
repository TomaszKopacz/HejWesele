package com.hejwesele.android.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationImpl @Inject constructor() : Navigator, Navigation {
    private val _events = MutableSharedFlow<Destination>()
    override val events: SharedFlow<Destination> = _events.asSharedFlow()

    override suspend fun navigate(destination: Destination) {
        _events.emit(destination)
    }
}
