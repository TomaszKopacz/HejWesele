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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.Footer
import com.hejwesele.android.components.HeaderLarge
import com.hejwesele.settings.navigation.SettingsNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(route = "main")
@SettingsNavGraph(start = true)
internal fun Settings(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = useDarkIcons)
    }

    val settingsUiState by settingsViewModel.states.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = WindowInsets.systemBars.asPaddingValues()
    ) {
        item {
            HeaderLarge(text = stringResource(id = R.string.settings_title))
        }
        settingsUiState.items.entries.forEach { (type, item) ->
            item {
                ListItem(
                    modifier = Modifier.clickable { settingsViewModel.onItemClicked(type) },
                    icon = { ListItemIcon(imageVector = item.icon) },
                    text = { Text(stringResource(id = item.text)) },
                    secondaryText = item.secondaryText?.let { {Text(text = stringResource(id = it)) } }
                )
            }
        }
        item {
            Footer(
                modifier = Modifier.padding(top = 8.dp),
                text = settingsUiState.appInformation
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
