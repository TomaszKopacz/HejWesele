package com.hejwesele.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.config.ConfigurationSwitcher.Switcher
import com.hejwesele.android.mvvm.ActionsEffect
import com.hejwesele.authentication.AuthenticationUiAction.ShowLoggingInMessage
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
internal fun Authentication(viewModel: AuthenticationViewModel = hiltViewModel()) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = useDarkIcons)
    }

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val uiState by viewModel.states.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                hostState = it
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Switcher(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding(),
                onClick = { /*navController.navigate(AuthenticationRoutes.configuration)*/ }
            )
            LoadingButton(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(fraction = 0.4f),
                isLoading = uiState.isAuthenticating,
                onClick = viewModel::authenticate
            ) {
                Text(stringResource(id = R.string.login))
            }
        }
    }

    val loggingInMessage = stringResource(id = R.string.loggingInMessage)
    ActionsEffect(viewModel, coroutineScope) { action ->
        when (action) {
            is ShowLoggingInMessage -> scaffoldState.snackbarHostState.showSnackbar(message = loggingInMessage)
        }
    }
}

@Composable
private fun LoadingButton(
    modifier: Modifier,
    isLoading: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier.height(44.dp),
        enabled = !isLoading,
        onClick = onClick
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(30.dp))
        } else {
            content.invoke(this)
        }
    }
}
