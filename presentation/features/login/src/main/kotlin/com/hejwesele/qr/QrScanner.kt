package com.hejwesele.qr

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.ILoginNavigation
import com.hejwesele.android.components.DismissiveError
import com.hejwesele.android.components.ErrorDialog
import com.hejwesele.android.components.HyperlinkText
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.components.PlainButton
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.components.layouts.BottomSheetScaffold
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
import kotlinx.coroutines.launch

@Composable
@Destination
fun QrScanner(navigation: ILoginNavigation) {
    QrScannerEntryPoint(navigation = navigation)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun QrScannerEntryPoint(
    navigation: ILoginNavigation,
    viewModel: QrScannerViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.states.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    QrScannerEventHandler(
        uiState = uiState,
        viewModel = viewModel,
        sheetState = sheetState,
        navigation = navigation
    )

    QrScannerScreen(
        isLoading = uiState.isLoading,
        dismissiveError = uiState.dismissiveError,
        sheetState = sheetState,
        isInternetPopupEnabled = true,
        onBack = { viewModel.onBackClicked() },
        onScanned = { text -> viewModel.onQrScanned(text) },
        onTermsAndConditionsLinkClicked = { viewModel.onTermsAndConditionsRequested() },
        onTermsAndConditionsAccepted = { viewModel.onTermsAndConditionsPromptAccepted() },
        onTermsAndConditionsDeclined = { viewModel.onTermsAndConditionsPromptDeclined() }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun QrScannerEventHandler(
    uiState: QrScannerUiState,
    viewModel: QrScannerViewModel,
    sheetState: ModalBottomSheetState,
    navigation: ILoginNavigation
) {
    EventEffect(
        event = uiState.navigateUp,
        onConsumed = { viewModel.onNavigatedUp() },
        action = { navigation.navigateUp() }
    )
    EventEffect(
        event = uiState.showTermsAndConditionsPrompt,
        onConsumed = { viewModel.onTermsAndConditionsPromptShown() },
        action = { sheetState.show() }
    )
    EventEffect(
        event = uiState.hideTermsAndConditionsPrompt,
        onConsumed = { viewModel.onTermsAndConditionsPromptHidden() },
        action = { sheetState.hide() }
    )
    EventEffect(
        event = uiState.openTermsAndConditions,
        onConsumed = { viewModel.onTermsAndConditionsOpened() },
        action = { navigation.openTermsAndConditions() }
    )
    EventEffect(
        event = uiState.openEvent,
        onConsumed = { viewModel.onEventOpened() },
        action = { navigation.openEvent() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun QrScannerScreen(
    isLoading: Boolean,
    dismissiveError: DismissiveError?,
    sheetState: ModalBottomSheetState,
    isInternetPopupEnabled: Boolean,
    onBack: () -> Unit,
    onScanned: (String) -> Unit,
    onTermsAndConditionsLinkClicked: () -> Unit,
    onTermsAndConditionsAccepted: () -> Unit,
    onTermsAndConditionsDeclined: () -> Unit
) {
    BottomSheetScaffold(
        state = sheetState,
        sheetContent = {
            TermsAndConditionsBottomSheetContent(
                onLinkClicked = onTermsAndConditionsLinkClicked,
                onDecline = onTermsAndConditionsDeclined,
                onAccept = onTermsAndConditionsAccepted
            )
        }
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
            if (dismissiveError != null) {
                ErrorDialog(error = dismissiveError)
            }
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

@Composable
private fun TermsAndConditionsBottomSheetContent(
    onLinkClicked: () -> Unit,
    onDecline: () -> Unit,
    onAccept: () -> Unit
) {
    Text(
        text = Label.loginScanQrTermsAndConditionsPromptTitleText,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    VerticalMargin(Dimension.marginNormal)
    HyperlinkText(
        text = Label.loginScanQrTermsAndConditionsPromptText,
        links = mapOf(Label.loginTermsAndConditionsClickableText to onLinkClicked),
        style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onSurface),
        linkStyle = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.outline)
    )
    VerticalMargin(Dimension.marginLarge)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        PlainButton(
            text = Label.cancel,
            color = MaterialTheme.colorScheme.primary,
            onClick = onDecline
        )
        PlainButton(
            text = Label.loginScanQrTermsAndConditionsAcceptText,
            color = MaterialTheme.colorScheme.primary,
            onClick = onAccept
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun QrScannerScreenPreview() {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)

    AppTheme(darkTheme = false) {
        QrScannerScreen(
            isLoading = false,
            dismissiveError = null,
            sheetState = sheetState,
            isInternetPopupEnabled = false,
            onBack = {},
            onScanned = {},
            onTermsAndConditionsLinkClicked = {},
            onTermsAndConditionsAccepted = {},
            onTermsAndConditionsDeclined = {}
        )
    }
}
