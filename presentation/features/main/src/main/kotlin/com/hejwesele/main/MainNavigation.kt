package com.hejwesele.main

import androidx.compose.runtime.Composable

interface IMainFeatureProvider {
    fun home(): @Composable () -> Unit
    fun schedule(): @Composable () -> Unit
    fun services(): @Composable () -> Unit
    fun gallery(): @Composable () -> Unit
}

internal object MainRoutes {
    const val home = "home"
    const val schedule = "schedule"
    const val services = "services"
    const val gallery = "gallery"
}
