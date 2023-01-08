package com.miquido.android.config.switcher

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jakewharton.processphoenix.ProcessPhoenix
import com.miquido.android.config.switcher.ConfigurationItem.RadioGroup
import com.miquido.android.config.switcher.ConfigurationUiState.Loading
import com.miquido.android.mvvm.ActionsEffect

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.configurationScreen(route: String) = composable(route) { ConfigurationScreen(viewModel = hiltViewModel()) }

@Composable
internal fun ConfigurationScreen(viewModel: ConfigurationViewModel) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.states.collectAsState(initial = Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuration") },
                contentPadding = WindowInsets
                    .statusBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                    .asPaddingValues()
            )
        },
        bottomBar = {
            Spacer(
                Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
            )
        }
    ) { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            when (val current = state) {
                is Loading -> LoadingState()
                is ConfigurationUiState.Loaded -> LoadedState(
                    items = current.items,
                    onOptionChecked = { id, name -> viewModel.onRadioChecked(id, name) },
                    onSaveClicked = { viewModel.onSaveClicked() }
                )
            }
        }

        ActionsEffect(viewModel, coroutineScope) { action ->
            when (action) {
                ConfigurationUiAction.Close -> ProcessPhoenix.triggerRebirth(context)
            }
        }
    }
}

@Composable
internal fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun LoadedState(
    items: List<ConfigurationItem>,
    onOptionChecked: (id: ConfigurationId, name: String) -> Unit,
    onSaveClicked: () -> Unit
) = Column(modifier = Modifier.padding(16.dp)) {
    items.forEach { item ->
        when (item) {
            is RadioGroup -> Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
                item.options.forEach {
                    Row(
                        modifier = Modifier.clickable { onOptionChecked(item.id, it.name) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = it.checked, onClick = { onOptionChecked(item.id, it.name) })
                        Text(text = it.name, modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
    Button(
        modifier = Modifier.padding(vertical = 16.dp),
        onClick = { onSaveClicked() }
    ) {
        Text(text = "Save")
    }
}
