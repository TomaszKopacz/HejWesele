package com.hejwesele.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.Footer
import com.hejwesele.android.components.HeaderLarge
import com.hejwesele.settings.appinfo.AppInfoViewModel
import com.hejwesele.settings.logout.LogoutViewModel
import com.hejwesele.settings.navigation.SettingsNavGraph
import com.hejwesele.settings.theme.ThemeInfoViewModel
import com.hejwesele.settings.theme.themeText
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(route = "main")
@SettingsNavGraph(start = true)
internal fun Settings(
    themeInfoViewModel: ThemeInfoViewModel = hiltViewModel(),
    appInfoViewModel: AppInfoViewModel = hiltViewModel(),
    logoutViewModel: LogoutViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = useDarkIcons)
    }

    val selectedTheme by remember(themeInfoViewModel) { themeInfoViewModel.observeSelectedTheme() }
        .collectAsState(initial = null)

    val appInfoUiState by appInfoViewModel.states.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = WindowInsets.systemBars.asPaddingValues()
    ) {
        item {
            HeaderLarge(text = stringResource(id = R.string.settings_title))
        }
        item {
            ListItem(
                modifier = Modifier.clickable { /*navController.navigate(SettingsRoutes.theme)*/ },
                icon = { ListItemIcon(imageVector = Icons.Filled.Palette) },
                text = { Text(stringResource(id = R.string.settings_theme)) },
                secondaryText = { Text(text = themeText(selectedTheme)) }
            )
        }
        item {
            ListItem(
                modifier = Modifier.clickable { /*navController.navigate(SettingsRoutes.licenses)*/ },
                icon = { ListItemIcon(imageVector = Icons.Filled.Info) },
                text = { Text(stringResource(id = R.string.settings_licenses)) }
            )
        }
        item {
            ListItem(
                modifier = Modifier.clickable { logoutViewModel.logout() },
                icon = { ListItemIcon(imageVector = Icons.Filled.Logout) },
                text = { Text(stringResource(id = R.string.settings_logout)) }
            )
        }
        item {
            Footer(
                modifier = Modifier.padding(top = 8.dp),
                text = appInfoUiState.appInformation
            )
        }
    }
}

@Composable
private fun ListItemIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        tint = MaterialTheme.colors.primary.copy(alpha = 0.8f),
        contentDescription = null
    )
}
