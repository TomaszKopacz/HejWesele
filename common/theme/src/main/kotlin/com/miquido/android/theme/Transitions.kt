package com.miquido.android.theme

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object Transitions {
    private const val DEFAULT_TIME = 250

    val fadeIn = fadeIn(animationSpec = tween(DEFAULT_TIME))
    val fadeOut = fadeOut(animationSpec = tween(DEFAULT_TIME))
    val slideInHorizontally = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(DEFAULT_TIME))
    val slideOutHorizontally = slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(DEFAULT_TIME))
    val slideInVertically = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(DEFAULT_TIME))
    val slideOutVertically = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(DEFAULT_TIME))
}
