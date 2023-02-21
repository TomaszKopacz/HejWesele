package com.hejwesele.android

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.R
import com.hejwesele.android.customtabs.CustomTabs
import com.hejwesele.android.customtabs.LocalCustomTabs
import com.hejwesele.android.navigation.Navigation
import com.hejwesele.android.theme.Transitions
import com.hejwesele.gallery.destinations.GalleryDestination
import com.hejwesele.home.destinations.HomeDestination
import com.hejwesele.schedule.destinations.ScheduleDestination
import com.hejwesele.services.destinations.ServicesDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavGraphSpec

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
internal fun AppRoot(
    navGraph: NavGraphSpec,
    navigation: Navigation,
    customTabs: CustomTabs,
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = useDarkIcons)
    }

    CompositionLocalProvider(LocalCustomTabs provides customTabs) {
        val navController = rememberAnimatedNavController()
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        navController.navigatorProvider += bottomSheetNavigator

        LaunchedEffect(navController) {
            navigation.actions.collect { navController.navigate(it.route, it.options) }
        }

        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            )
        ) {
            Scaffold(
                bottomBar = {
                    AppBottomBar(
                        items = bottomNavItems,
                        navController = navController
                    )
                }
            ) { innerPadding ->
               DestinationsNavHost(
                   engine = rememberAnimatedNavHostEngine(
                       rootDefaultAnimations = RootNavGraphDefaultAnimations(
                           enterTransition = { Transitions.fadeIn },
                           exitTransition = { Transitions.fadeOut }
                       )
                   ),
                   navController = navController,
                   navGraph = navGraph,
                   startRoute = navGraph.startRoute,
                   modifier = Modifier.padding(innerPadding)
               )
            }
        }
    }
}

private val bottomNavItems = setOf(
    BottomNavItem(
        route = HomeDestination.route,
        label = R.string.home,
        icon = Icons.Filled.Home
    ),
    BottomNavItem(
        route = ScheduleDestination.route,
        label = R.string.schedule,
        icon = Icons.Filled.Home
    ),
    BottomNavItem(
        route = ServicesDestination.route,
        label = R.string.services,
        icon = Icons.Filled.Home
    ),
    BottomNavItem(
        route = GalleryDestination.route,
        label = R.string.gallery,
        icon = Icons.Filled.Home
    )
)