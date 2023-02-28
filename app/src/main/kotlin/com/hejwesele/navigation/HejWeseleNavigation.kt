package com.hejwesele.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
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

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun HejWeseleNavigation() {
    val navController = rememberAnimatedNavController()
    DestinationsNavHost(
        navGraph = root,
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

private val root = object : NavGraphSpec {
    override val route = "rootNavGraph"
    override val startRoute = MainNavGraph
    override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
    override val nestedNavGraphs = listOf(
        MainNavGraph,
        SettingsNavGraph
    )
}
