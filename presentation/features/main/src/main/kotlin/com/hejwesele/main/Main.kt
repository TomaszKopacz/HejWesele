package com.hejwesele.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Transitions
import com.hejwesele.main.MainTabItem.Gallery
import com.hejwesele.main.MainTabItem.Home
import com.hejwesele.main.MainTabItem.Schedule
import com.hejwesele.main.MainTabItem.Services
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun Main(featureProvider: IMainFeatureProvider) {
    MainScreen(
        home = featureProvider.home(),
        schedule = featureProvider.schedule(),
        services = featureProvider.services(),
        gallery = featureProvider.gallery()
    )
}

@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun MainScreen(
    home: @Composable () -> Unit = {},
    schedule: @Composable () -> Unit = {},
    services: @Composable () -> Unit = {},
    gallery: @Composable () -> Unit = {}
) {
    val systemUiController = rememberSystemUiController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
        systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = false)
    }

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
        bottomBar = { MainBottomNavigation(navController) },
        contentWindowInsets = WindowInsets(top = 0.dp)
    ) { innerPadding ->
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            modifier = Modifier.padding(innerPadding)
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = MainRoutes.home,
                enterTransition = { Transitions.fadeIn },
                exitTransition = { Transitions.fadeOut }
            ) {
                composable(MainRoutes.home) { home() }
                composable(MainRoutes.schedule) { schedule() }
                composable(MainRoutes.services) { services() }
                composable(MainRoutes.gallery) { gallery() }
            }
        }
    }
}

@Composable
private fun MainBottomNavigation(navController: NavController) {
    BottomNavigation(
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        val mainTabItems = listOf(
            Home,
            Schedule,
            Services,
            Gallery
        )

        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination

        mainTabItems.forEach { item ->
            MainBottomNavigationItem(
                item = item,
                isItemSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onItemSelected = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.MainBottomNavigationItem(
    item: MainTabItem,
    isItemSelected: Boolean,
    onItemSelected: () -> Unit
) {
    BottomNavigationItem(
        icon = {
            val iconResId = if (isItemSelected) item.iconSelected else item.iconUnselected
            val iconTint = if (isItemSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

            Icon(
                painter = painterResource(iconResId),
                tint = iconTint,
                modifier = Modifier.size(Dimension.iconSizeNormal),
                contentDescription = null
            )
        },
        selected = isItemSelected,
        onClick = onItemSelected,
        selectedContentColor = MaterialTheme.colorScheme.primary
    )
}
