package com.hejwesele.event.navigation

import androidx.compose.runtime.Composable

interface MainFeatureProvider {
    fun home(): @Composable () -> Unit
    fun schedule(): @Composable () -> Unit
    fun services(): @Composable () -> Unit
    fun gallery(): @Composable () -> Unit
}
