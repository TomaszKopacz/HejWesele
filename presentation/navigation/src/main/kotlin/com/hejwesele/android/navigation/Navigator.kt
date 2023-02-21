package com.hejwesele.android.navigation

import androidx.navigation.NavOptionsBuilder

interface Navigator {
    suspend fun navigate(destination: Destination)

    suspend fun navigate(direction: ComposeDirection, builder: NavOptionsBuilder.() -> Unit = {})
}
