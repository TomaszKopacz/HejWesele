package com.miquido.androidtemplate.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TestAnimatedNavHost(
    vararg navigators: Navigator<out NavDestination>,
    builder: NavGraphBuilder.(String, NavHostController) -> Unit
) {
    val navController = rememberAnimatedNavController(*navigators)
    val startRoute = "/start"

    AnimatedNavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        builder(startRoute, navController)
    }
}
