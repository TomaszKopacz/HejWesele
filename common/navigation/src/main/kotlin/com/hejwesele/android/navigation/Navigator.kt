package com.hejwesele.android.navigation

interface Navigator {
    suspend fun navigate(destination: Destination)
}
