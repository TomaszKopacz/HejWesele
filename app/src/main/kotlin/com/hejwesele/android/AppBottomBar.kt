package com.hejwesele.android

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.ui.BottomNavigation

@Composable
fun AppBottomBar(items: Set<BottomNavItem>, navController: NavController) {
    val currBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currBackStackEntry?.destination

    val bottomRoutes = remember(items) { items.map { it.route } }
    val currentRouteHierarchy = remember(currentDestination) {
        currentDestination?.hierarchy?.mapNotNull { it.route }.orEmpty().toSet()
    }

    if (bottomRoutes.intersect(currentRouteHierarchy).isEmpty()) return

    BottomNavigation(contentPadding = WindowInsets.navigationBars.asPaddingValues()) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(item.label)) },
                selected = currentRouteHierarchy.any { it == item.route },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(items.first().route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}