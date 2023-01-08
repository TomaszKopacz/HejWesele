package com.miquido.androidtemplate.authentication

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.miquido.android.config.switcher.configurationScreen
import com.miquido.android.theme.Transitions

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authenticationGraph(route: String, navController: NavHostController) {
    navigation(startDestination = AuthenticationRoutes.main, route = route) {
        composable(
            route = AuthenticationRoutes.main,
            exitTransition = {
                if (targetState.destination.isOutsideOfAuthGraph(route)) {
                    Transitions.slideOutHorizontally
                } else {
                    null
                }
            }
        ) {
            Authentication(viewModel = hiltViewModel(), navController)
        }
        configurationScreen(AuthenticationRoutes.configuration)
    }
}

private fun NavDestination.isOutsideOfAuthGraph(authRoute: String) = hierarchy.none { it.route == authRoute }
