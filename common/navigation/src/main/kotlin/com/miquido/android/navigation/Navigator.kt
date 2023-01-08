package com.miquido.android.navigation

interface Navigator {
    suspend fun navigate(destination: Destination)
}
