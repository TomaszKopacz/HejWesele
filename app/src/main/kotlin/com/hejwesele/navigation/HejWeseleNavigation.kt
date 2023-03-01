package com.hejwesele.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.hejwesele.MainActivityViewModel
import com.hejwesele.android.theme.Transitions
import com.hejwesele.event.navigation.MainFeatureProvider
import com.hejwesele.gallery.navigation.GalleryFeatureNavigation
import com.hejwesele.home.navigation.HomeFeatureNavigation
import com.hejwesele.schedule.navigation.ScheduleFeatureNavigation
import com.hejwesele.settings.navigation.SettingsFeatureNavigation
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
internal fun HejWeseleNavigation(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val navController = rememberAnimatedNavController()
    val uiState  by viewModel.states.collectAsState()

    uiState.startRoute?.let {
        DestinationsNavHost(
            navGraph = getRootGraph(it),
            engine = rememberAnimatedNavHostEngine(
                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = { Transitions.fadeIn },
                    exitTransition = { Transitions.fadeOut }
                )
            ),
            navController = navController,
            dependenciesContainerBuilder = provideDependencies(navController = navController)
        )
    }
}

private fun provideDependencies(
    navController: NavHostController
): @Composable (DependenciesContainerBuilder<*>.() -> Unit) = {

    val homeNavigation: HomeFeatureNavigation = HomeFeatureNavigationImpl(navController)
    val scheduleNavigation: ScheduleFeatureNavigation = ScheduleFeatureNavigationImpl(navController)
    val galleryNavigation: GalleryFeatureNavigation = GalleryFeatureNavigationImpl(navController)
    val mainFeatureProvider: MainFeatureProvider = MainFeatureProviderImpl(
        homeNavigation,
        scheduleNavigation,
        galleryNavigation
    )
    val settingsNavigation: SettingsFeatureNavigation = SettingsFeatureNavigationImpl(navController)

    dependency(mainFeatureProvider)
    dependency(homeNavigation)
    dependency(scheduleNavigation)
    dependency(galleryNavigation)
    dependency(settingsNavigation)
}

private fun getRootGraph(startRoute: Route) = object : NavGraphSpec {
    override val route = "rootNavGraph"
    override val startRoute = startRoute
    override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
    override val nestedNavGraphs = listOf(
        MainNavGraph,
        SettingsNavGraph
    )
}
