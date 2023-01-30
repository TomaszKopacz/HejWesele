package com.hejwesele.event

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
import com.hejwesele.gallery.Gallery
import com.hejwesele.home.navigation.homeGraph
import com.hejwesele.schedule.Schedule
import com.hejwesele.services.Services

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Event(eventId: Int) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
        systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = false)
    }

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
        bottomBar = { EventBottomNavigation(navController) },
        contentWindowInsets = WindowInsets(top = 0.dp)
    ) { innerPadding ->
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            modifier = Modifier.padding(innerPadding)
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = EventRoutes.home,
                enterTransition = { Transitions.fadeIn },
                exitTransition = { Transitions.fadeOut }
            ) {
                homeGraph(
                    route = EventRoutes.home
                )
                composable(EventRoutes.schedule) { Schedule() }
                composable(EventRoutes.services) { Services() }
                composable(EventRoutes.gallery) { Gallery() }
            }
        }
    }
}

@Composable
internal fun EventBottomNavigation(navController: NavController) {
    BottomNavigation(
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        val eventTabItems = listOf(
            EventTabItem.Home,
            EventTabItem.Schedule,
            EventTabItem.Services,
            EventTabItem.Gallery
        )

        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination

        eventTabItems.forEach { item ->
            EventBottomNavigationItem(
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
internal fun RowScope.EventBottomNavigationItem(
    item: EventTabItem,
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
