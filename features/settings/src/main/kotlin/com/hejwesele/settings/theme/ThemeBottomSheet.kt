package com.hejwesele.settings.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hejwesele.android.ui.HeaderSmall
import com.hejwesele.settings.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ThemeBottomSheet(viewModel: ThemeBottomSheetViewModel) {
    val uiState by viewModel.states.collectAsState()
    val selectedTheme by remember(viewModel) { viewModel.observeSelectedTheme() }
        .collectAsState(initial = null)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            HeaderSmall(text = stringResource(id = R.string.theme_title))
        }
        uiState.themes.forEach { theme ->
            item(theme.name) {
                ListItem(
                    modifier = Modifier.clickable { viewModel.switchTheme(theme) },
                    icon = { SelectedIcon(theme == selectedTheme) },
                    text = { Text(text = themeText(theme)) }
                )
            }
        }
    }
}

@Composable
private fun SelectedIcon(isSelected: Boolean) {
    if (isSelected) {
        val color = LocalContentColor.current
        Icon(
            imageVector = Icons.Filled.Check,
            tint = color,
            contentDescription = null
        )
    } else {
        Spacer(modifier = Modifier)
    }
}
