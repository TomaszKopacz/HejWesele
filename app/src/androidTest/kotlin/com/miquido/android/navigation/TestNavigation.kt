package com.miquido.android.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object TestNavigation : Navigator, Navigation {
    private val _events = MutableSharedFlow<Destination>()
    override val events: SharedFlow<Destination> = _events.asSharedFlow()

    private val _navigatedTo = mutableListOf<Destination>()
    val navigatedTo: List<Destination> = _navigatedTo

    override suspend fun navigate(destination: Destination) {
        _events.emit(destination)
        _navigatedTo.add(destination)
    }
}
