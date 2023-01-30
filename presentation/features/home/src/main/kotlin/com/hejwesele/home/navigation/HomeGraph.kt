package com.hejwesele.home.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.hejwesele.home.Home

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.homeGraph(route: String) {
    navigation(startDestination = HomeRoutes.main, route = route) {
        composable(HomeRoutes.main) {
            Home(
                viewModel = hiltViewModel()
            )
        }
    }
}
