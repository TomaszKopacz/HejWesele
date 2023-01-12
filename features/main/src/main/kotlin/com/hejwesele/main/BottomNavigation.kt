@file:Suppress("MatchingDeclarationName")

package com.hejwesele.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.hejwesele.main.R
import com.hejwesele.main.BottomNavigationItem.Dashboard
import com.hejwesele.main.BottomNavigationItem.Settings

internal sealed class BottomNavigationItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    object Dashboard : BottomNavigationItem(
        route = MainRoutes.dashboard,
        icon = Icons.Filled.Home,
        label = R.string.dashboard
    )

    object Settings : BottomNavigationItem(
        route = MainRoutes.settings,
        icon = Icons.Filled.Settings,
        label = R.string.settings
    )
}

internal val bottomNavigationItems = listOf(
    Dashboard,
    Settings
)
