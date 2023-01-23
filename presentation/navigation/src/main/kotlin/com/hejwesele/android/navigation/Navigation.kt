package com.hejwesele.android.navigation

import kotlinx.coroutines.flow.SharedFlow

interface Navigation {
    val events: SharedFlow<Destination>
}
