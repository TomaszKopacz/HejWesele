package com.hejwesele.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.hejwesele.android.theme.Transitions
import com.hejwesele.settings.licenses.Licenses
import com.hejwesele.settings.theme.ThemeBottomSheet

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.settingsGraph(route: String, navController: NavHostController) {
    navigation(startDestination = SettingsRoutes.main, route = route) {
        composable(SettingsRoutes.main) {
            Settings(
                themeInfoViewModel = hiltViewModel(),
                appInfoViewModel = hiltViewModel(),
                logoutViewModel = hiltViewModel()
            )
        }
        bottomSheet(SettingsRoutes.theme) {
            ThemeBottomSheet(
                viewModel = hiltViewModel()
            )
        }
        composable(
            route = SettingsRoutes.licenses,
            enterTransition = { Transitions.slideInVertically },
            exitTransition = { Transitions.slideOutVertically }
        ) {
            Licenses(viewModel = hiltViewModel())
        }
    }
}
