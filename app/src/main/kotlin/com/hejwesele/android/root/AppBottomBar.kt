package com.hejwesele.android.root

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.ui.BottomNavigation
import com.hejwesele.android.theme.Dimension

@Composable
fun AppBottomBar(items: Set<BottomNavItem>, navController: NavController) {
    val currBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currBackStackEntry?.destination

    val bottomRoutes = remember(items) { items.map { it.route } }
    val currentRouteHierarchy = remember(currentDestination) {
        currentDestination?.hierarchy?.mapNotNull { it.route }.orEmpty().toSet()
    }

    if (bottomRoutes.intersect(currentRouteHierarchy).isEmpty()) return

    BottomNavigation(
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val isSelected = currentRouteHierarchy.any { it == item.route }
            val iconResId = if (isSelected) item.iconSelected else item.iconUnselected
            val iconTint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(iconResId),
                        tint = iconTint,
                        modifier = Modifier.size(Dimension.iconSizeNormal),
                        contentDescription = null
                    )
                },
                selected = isSelected,
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