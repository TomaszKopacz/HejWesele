package com.hejwesele.qr

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.ILoginNavigation
import com.hejwesele.android.components.ErrorDialog
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.theme.md_theme_dark_onBackground
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.login.R
import com.hejwesele.qrscanner.QrScannerView
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun QrScanner(navigation: ILoginNavigation) {
    QrScannerEntryPoint(navigation = navigation)
}

@Composable
private fun QrScannerEntryPoint(
    navigation: ILoginNavigation,
    viewModel: QrScannerViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()

    QrScannerEventHandler(
        uiState = uiState,
        viewModel = viewModel,
        navigation = navigation
    )

    QrScannerScreen(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        isInternetPopupEnabled = true,
        onBack = { viewModel.onBackClicked() },
        onErrorDismiss = { viewModel.onErrorDismissed() },
        onScanned = { text -> viewModel.onQrScanned(text) }
    )
}

@Composable
private fun QrScannerEventHandler(
    uiState: QrScannerUiState,
    viewModel: QrScannerViewModel,
    navigation: ILoginNavigation
) {
    EventEffect(
        event = uiState.navigateUp,
        onConsumed = { viewModel.onNavigatedUp() },
        action = { navigation.navigateUp() }
    )
    EventEffect(
        event = uiState.openEvent,
        onConsumed = { viewModel.onEventOpened() },
        action = { navigation.openEvent() }
    )
}

@Composable
private fun QrScannerScreen(
    isLoading: Boolean,
    isError: Boolean,
    isInternetPopupEnabled: Boolean,
    onBack: () -> Unit,
    onErrorDismiss: () -> Unit,
    onScanned: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        QrScannerContent(
            modifier = Modifier.fillMaxSize(),
            isInternetPopupEnabled = isInternetPopupEnabled,
            onBack = onBack,
            onScanned = onScanned
        )
        if (isLoading) {
            LoaderDialog(label = Label.loginLoadingText)
        }
        if (isError) {
            ErrorDialog(
                onDismiss = onErrorDismiss
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun QrScannerContent(
    modifier: Modifier = Modifier,
    isInternetPopupEnabled: Boolean,
    onBack: () -> Unit,
    onScanned: (String) -> Unit
) {
    Scaffold { padding ->
        Column(modifier = modifier) {
            if (isInternetPopupEnabled) {
                InternetConnectionPopup(statusBarSensitive = false)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                QrScannerView(
                    modifier = Modifier.fillMaxSize(),
                    onScanned = { text -> onScanned(text) }
                )
                BackIcon(
                    modifier = Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                            start = Dimension.marginNormal,
                            end = Dimension.marginNormal,
                            bottom = Dimension.marginNormal
                        ),
                    onClick = onBack
                )
            }
        }
    }
}

@Composable
private fun BackIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Icon(
            modifier = Modifier
                .size(Dimension.iconNormal)
                .clickable { onClick() },
            contentDescription = null,
            painter = painterResource(R.drawable.ic_arrow_left),
            tint = md_theme_dark_onBackground
        )
    }
}

@Preview
@Composable
private fun QrScannerScreenPreview() {
    AppTheme(darkTheme = false) {
        QrScannerScreen(
            isLoading = false,
            isError = false,
            isInternetPopupEnabled = false,
            onBack = {},
            onErrorDismiss = {},
            onScanned = {}
        )
    }
}
