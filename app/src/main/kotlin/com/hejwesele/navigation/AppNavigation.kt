package com.hejwesele.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.hejwesele.ILoginNavigation
import com.hejwesele.android.theme.Transitions
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.destinations.PhotoConfirmationDestination
import com.hejwesele.home.IHomeNavigation
import com.hejwesele.information.IInformationFeatureNavigation
import com.hejwesele.main.IMainFeatureProvider
import com.hejwesele.navigation.features.GalleryFeatureNavigation
import com.hejwesele.navigation.features.HomeFeatureNavigation
import com.hejwesele.navigation.features.InformationFeatureNavigation
import com.hejwesele.navigation.features.LoginFeatureNavigation
import com.hejwesele.navigation.features.MainFeatureProvider
import com.hejwesele.navigation.features.ScheduleFeatureNavigation
import com.hejwesele.schedule.IScheduleNavigation
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.resultRecipient

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
internal fun AppNavigation(
    viewModel: AppNavigationViewModel = hiltViewModel()
) {
    val navController = rememberAnimatedNavController()
    val uiState by viewModel.states.collectAsState()

    uiState.startRoute?.let {
        DestinationsNavHost(
            navGraph = getRootGraph(it),
            engine = rememberAnimatedNavHostEngine(
                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = { Transitions.slideInHorizontally },
                    exitTransition = { Transitions.slideOutHorizontally }
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
    val loginFeatureNavigation: ILoginNavigation = LoginFeatureNavigation(navController)

    val homeNavigation: IHomeNavigation = HomeFeatureNavigation(navController)
    val scheduleNavigation: IScheduleNavigation = ScheduleFeatureNavigation(navController)
    val galleryNavigation: IGalleryNavigation = GalleryFeatureNavigation(navController)
    val galleryFeatureRecipient = resultRecipient<PhotoConfirmationDestination, Boolean>()
    val mainFeatureProvider: IMainFeatureProvider = MainFeatureProvider(
        homeFeatureNavigation = homeNavigation,
        scheduleFeatureNavigation = scheduleNavigation,
        galleryFeatureNavigation = galleryNavigation,
        galleryFeatureRecipient = galleryFeatureRecipient
    )

    val informationNavigation: IInformationFeatureNavigation = InformationFeatureNavigation(navController)

    dependency(loginFeatureNavigation)
    dependency(mainFeatureProvider)
    dependency(homeNavigation)
    dependency(scheduleNavigation)
    dependency(galleryNavigation)
    dependency(informationNavigation)
}
