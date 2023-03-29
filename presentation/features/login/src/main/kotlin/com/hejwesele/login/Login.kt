package com.hejwesele.login

import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.ILoginNavigation
import com.hejwesele.android.components.ErrorDialog
import com.hejwesele.android.components.FilledButton
import com.hejwesele.android.components.FormTextField
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.components.PlainIconButton
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.components.layouts.BottomSheetScaffold
import com.hejwesele.android.components.layouts.ScrollableColumn
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.extensions.noRippleClickable
import com.hejwesele.internet.InternetConnectionPopup
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@Composable
@Destination
fun Login(navigation: ILoginNavigation) {
    LoginEntryPoint(navigation)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LoginEntryPoint(
    navigation: ILoginNavigation,
    viewModel: LoginViewModel = hiltViewModel()
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

    val cameraPermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { isGranted ->
        viewModel.onCameraPermissionResult(isGranted)
    }

    LoginEventHandler(
        uiState = uiState,
        sheetState = sheetState,
        cameraPermissionLauncher = cameraPermissionLauncher,
        viewModel = viewModel,
        navigation = navigation
    )

    val actions = LoginActions(
        onNameInputChanged = { text -> viewModel.onNameInputChanged(text) },
        onPasswordInputChanged = { text -> viewModel.onPasswordInputChanged(text) },
        onHelpClicked = { viewModel.onHelpRequested() },
        onNextButtonClick = { viewModel.onSubmit() },
        onScanQrButtonClick = { viewModel.onScanQrClicked() },
        onErrorDismissed = { viewModel.onErrorDismissed() }
    )

    LoginScreen(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        isNextButtonEnabled = uiState.isFormValid,
        nameErrorMessage = uiState.eventNameError?.message,
        passwordErrorMessage = uiState.eventPasswordError?.message,
        isInternetPopupEnabled = true,
        sheetState = sheetState,
        actions = actions
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LoginEventHandler(
    uiState: LoginUiState,
    sheetState: ModalBottomSheetState,
    cameraPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    viewModel: LoginViewModel,
    navigation: ILoginNavigation
) {
    EventEffect(
        event = uiState.showHelp,
        onConsumed = { viewModel.onHelpShown() },
        action = { sheetState.show() }
    )
    EventEffect(
        event = uiState.openEvent,
        onConsumed = { viewModel.onEventOpened() },
        action = { navigation.openEvent() }
    )
    EventEffect(
        event = uiState.requestCameraPermission,
        onConsumed = { viewModel.onCameraPermissionRequested() },
        action = { permission -> cameraPermissionLauncher.launch(permission) }
    )
    EventEffect(
        event = uiState.openQrScanner,
        onConsumed = { viewModel.onQrScannerOpened() },
        action = { navigation.openQrScanner() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LoginScreen(
    isLoading: Boolean,
    isError: Boolean,
    isNextButtonEnabled: Boolean,
    nameErrorMessage: String?,
    passwordErrorMessage: String?,
    isInternetPopupEnabled: Boolean,
    sheetState: ModalBottomSheetState,
    actions: LoginActions
) {
    BottomSheetScaffold(
        state = sheetState,
        sheetContent = { HelpBottomSheetContent() }
    ) {
        LoginScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            isNextButtonEnabled = isNextButtonEnabled,
            nameErrorMessage = nameErrorMessage,
            passwordErrorMessage = passwordErrorMessage,
            isInternetPopupEnabled = isInternetPopupEnabled,
            onNameInputChanged = actions.onNameInputChanged,
            onPasswordInputChanged = actions.onPasswordInputChanged,
            onHelpClicked = actions.onHelpClicked,
            onNextButtonClicked = actions.onNextButtonClick,
            onScanQrButtonClicked = actions.onScanQrButtonClick
        )
        if (isError) {
            ErrorDialog(onDismiss = actions.onErrorDismissed)
        }
        if (isLoading) {
            LoaderDialog(label = Label.loginLoadingText)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoroutinesApi::class)
@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    isNextButtonEnabled: Boolean,
    nameErrorMessage: String?,
    passwordErrorMessage: String?,
    isInternetPopupEnabled: Boolean,
    onNameInputChanged: (String) -> Unit,
    onPasswordInputChanged: (String) -> Unit,
    onHelpClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onScanQrButtonClicked: () -> Unit
) {
    val bottomPadding = WindowInsets.navigationBars
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()
        .calculateBottomPadding()

    Column(modifier = modifier) {
        if (isInternetPopupEnabled) {
            InternetConnectionPopup()
        }
        VerticalMargin(Dimension.marginNormal)
        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = Dimension.marginLarge,
                    end = Dimension.marginLarge,
                    bottom = bottomPadding
                )
        ) {
            InfoIconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .size(Dimension.iconNormal)
                    .noRippleClickable(onClick = {})
            )
            VerticalMargin(Dimension.marginOutsizeLarge)
            Icon(
                modifier = Modifier.size(Dimension.iconOutsizeExtraLarge),
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            VerticalMargin(Dimension.marginLarge)
            Spacer(modifier = Modifier.weight(Dimension.weightHalf))
            LoginForm(
                isNextButtonEnabled = isNextButtonEnabled,
                nameErrorMessage = nameErrorMessage,
                passwordErrorMessage = passwordErrorMessage,
                onNameInputChanged = onNameInputChanged,
                onPasswordInputChanged = onPasswordInputChanged,
                onHelpClicked = onHelpClicked,
                onNextButtonClicked = onNextButtonClicked,
                onScanQrButtonClick = onScanQrButtonClicked
            )
            VerticalMargin(Dimension.marginNormal)
        }
    }
}

@Composable
private fun InfoIconButton(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_info),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun ColumnScope.LoginForm(
    isNextButtonEnabled: Boolean,
    nameErrorMessage: String?,
    passwordErrorMessage: String?,
    onNameInputChanged: (String) -> Unit,
    onPasswordInputChanged: (String) -> Unit,
    onHelpClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onScanQrButtonClick: () -> Unit
) {
    FormTextField(
        label = Label.loginEventNameLabel,
        imeAction = ImeAction.Next,
        isError = nameErrorMessage != null,
        errorMessage = nameErrorMessage,
        onTextChanged = { text -> onNameInputChanged(text) }
    )
    VerticalMargin(Dimension.marginExtraSmall)
    FormTextField(
        label = Label.loginEventPasswordLabel,
        imeAction = ImeAction.Done,
        isError = passwordErrorMessage != null,
        errorMessage = passwordErrorMessage,
        transformation = PasswordVisualTransformation(),
        onTextChanged = { text -> onPasswordInputChanged(text) }
    )
    VerticalMargin(Dimension.marginNormal)
    HelpLabel(
        modifier = Modifier
            .align(Alignment.Start)
            .noRippleClickable { onHelpClicked() }
    )
    VerticalMargin(Dimension.marginLarge)
    FilledButton(
        text = Label.next,
        enabled = isNextButtonEnabled,
        onClick = onNextButtonClicked
    )
    VerticalMargin(Dimension.marginLarge)
    Spacer(modifier = Modifier.weight(Dimension.weightFull))
    PlainIconButton(
        text = Label.loginScanQrButtonLabel,
        icon = R.drawable.ic_qr,
        color = MaterialTheme.colorScheme.primary,
        onClick = onScanQrButtonClick
    )
}

@Composable
private fun HelpLabel(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(Dimension.iconSmall),
            painter = painterResource(R.drawable.ic_question),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        HorizontalMargin(Dimension.marginExtraSmall)
        Text(
            text = Label.loginHelpText,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun HelpBottomSheetContent() {
    Text(
        text = Label.loginHelpBottomSheetTitleText,
        style = MaterialTheme.typography.titleMedium
    )
    VerticalMargin(Dimension.marginNormal)
    Text(
        text = Label.loginHelpBottomSheetDescriptionText,
        style = MaterialTheme.typography.bodyMedium
    )
    VerticalMargin(Dimension.marginNormal)
}

private data class LoginActions(
    val onNameInputChanged: (String) -> Unit,
    val onPasswordInputChanged: (String) -> Unit,
    val onHelpClicked: () -> Unit,
    val onNextButtonClick: () -> Unit,
    val onScanQrButtonClick: () -> Unit,
    val onErrorDismissed: () -> Unit
)

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun LoginScreenPreview() {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)

    AppTheme(darkTheme = false) {
        LoginScreen(
            isLoading = false,
            isError = false,
            isNextButtonEnabled = true,
            nameErrorMessage = null,
            passwordErrorMessage = null,
            isInternetPopupEnabled = false,
            sheetState = sheetState,
            actions = LoginActions(
                onNameInputChanged = {},
                onPasswordInputChanged = {},
                onHelpClicked = {},
                onNextButtonClick = {},
                onScanQrButtonClick = {},
                onErrorDismissed = {}
            )
        )
    }
}
