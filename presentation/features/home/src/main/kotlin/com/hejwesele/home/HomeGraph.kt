package com.hejwesele.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.homeGraph(route: String, navController: NavHostController, eventId: Int) {
    navigation(startDestination = HomeRoutes.main, route = route) {
        composable(HomeRoutes.main) {
            Home(
                eventId = eventId,
                viewModel = hiltViewModel()
            )
        }
    }
}
