package com.hejwesele.android.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.R
import com.hejwesele.android.customtabs.CustomTabs
import com.hejwesele.android.customtabs.LocalCustomTabs
import com.hejwesele.android.navigation.NavAction
import com.hejwesele.android.navigation.Navigation
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Transitions
import com.hejwesele.android.thememanager.Theme
import com.hejwesele.android.thememanager.ThemeManager
import com.hejwesele.gallery.destinations.GalleryDestination
import com.hejwesele.home.destinations.HomeDestination
import com.hejwesele.schedule.destinations.ScheduleDestination
import com.hejwesele.services.destinations.ServicesDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavGraphSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppRoot(
    navController: NavHostController = rememberAnimatedNavController(),
    navGraph: NavGraphSpec,
    navigation: Navigation,
    customTabs: CustomTabs,
    themeManager: ThemeManager
) {
    ApplicationTheme(themeManager) {
        SystemBars()
        CompositionLocalProvider(LocalCustomTabs provides customTabs) {
            AppNavigation(
                navController = navController,
                navGraph = navGraph,
                navigation = navigation
            )
        }
    }
}

@Composable
private fun ApplicationTheme(themeManager: ThemeManager, content: @Composable () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val themeFlow = themeManager.observeSelectedTheme()
    val themeFlowLifecycleAware = remember(themeFlow, lifecycleOwner) {
        themeFlow.flowWithLifecycle(lifecycleOwner.lifecycle, STARTED)
    }

    val theme by themeFlowLifecycleAware.collectAsState(themeManager.getSelectedTheme())

    AppTheme(darkTheme = isAppIndDarkTheme(theme), content = content)
}

@Composable
private fun SystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = useDarkIcons)
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
private fun AppNavigation(
    navController: NavHostController,
    navGraph: NavGraphSpec,
    navigation: Navigation
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator

    NavActionsEffect(
        navController = navController,
        navigation = navigation
    )

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
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun NavActionsEffect(
    navController: NavHostController,
    navigation: Navigation
) {
    LaunchedEffect(navController) {
        navigation.navActions.collect { action ->
            when (action) {
                is NavAction.To -> navController.navigate(action.direction.route, action.options)
                is NavAction.Pop -> navController.popBackStack(action.route.route, action.inclusive, action.saveState)
                is NavAction.Up -> navController.navigateUp()
            }
        }
    }
}

@Composable
private fun isAppIndDarkTheme(theme: Theme): Boolean = when (theme) {
    Theme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    Theme.LIGHT -> true
    Theme.DARK -> false
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