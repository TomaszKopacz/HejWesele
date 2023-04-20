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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Checkbox
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
import com.hejwesele.android.components.HyperlinkText
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

    val formData = LoginFormData(
        isNextButtonEnabled = uiState.isFormValid,
        nameText = uiState.eventNameInput,
        passwordText = uiState.eventPasswordInput,
        nameErrorMessage = uiState.eventNameError?.message,
        passwordErrorMessage = uiState.eventPasswordError?.message,
        termsAndConditionsChecked = uiState.termsAndConditionsAccepted
    )

    val actions = LoginActions(
        onNameInputChanged = { text -> viewModel.onNameInputChanged(text) },
        onPasswordInputChanged = { text -> viewModel.onPasswordInputChanged(text) },
        onInfoClicked = { viewModel.onSettingsRequested() },
        onHelpClicked = { viewModel.onHelpRequested() },
        onTermsAndConditionsCheckedChanged = { viewModel.onTermsAndConditionsCheckedChanged(it) },
        onTermsAndConditionsLinkClicked = { viewModel.onTermsAndConditionsRequested() },
        onNextButtonClicked = { viewModel.onSubmit() },
        onScanQrButtonClicked = { viewModel.onScanQrClicked() }
    )

    LoginScreen(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        formData = formData,
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
        event = uiState.openSettings,
        onConsumed = { viewModel.onSettingsOpened() },
        action = { navigation.openSettings() }
    )
    EventEffect(
        event = uiState.showHelp,
        onConsumed = { viewModel.onHelpShown() },
        action = { sheetState.show() }
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
    formData: LoginFormData,
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
            formData = formData,
            actions = actions,
            isInternetPopupEnabled = isInternetPopupEnabled
        )
        if (isError) {
            ErrorDialog()
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
    formData: LoginFormData,
    actions: LoginActions,
    isInternetPopupEnabled: Boolean
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
                    .noRippleClickable(onClick = { actions.onInfoClicked() })
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
                formData = formData,
                actions = actions
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
    formData: LoginFormData,
    actions: LoginActions
) {
    FormTextField(
        label = Label.loginEventNameLabel,
        text = formData.nameText,
        imeAction = ImeAction.Next,
        isError = formData.nameErrorMessage != null,
        errorMessage = formData.nameErrorMessage,
        onTextChanged = { text -> actions.onNameInputChanged(text) }
    )
    VerticalMargin(Dimension.marginExtraSmall)
    FormTextField(
        label = Label.loginEventPasswordLabel,
        text = formData.passwordText,
        imeAction = ImeAction.Done,
        isError = formData.passwordErrorMessage != null,
        errorMessage = formData.passwordErrorMessage,
        transformation = PasswordVisualTransformation(),
        onTextChanged = { text -> actions.onPasswordInputChanged(text) }
    )
    VerticalMargin(Dimension.marginNormal)
    HelpLabel(
        modifier = Modifier
            .align(Alignment.Start)
            .noRippleClickable { actions.onHelpClicked() }
    )
    VerticalMargin(Dimension.marginNormal)
    TermsAndConditionsCheckbox(
        modifier = Modifier.fillMaxWidth(),
        checked = formData.termsAndConditionsChecked,
        onCheckedChanged = { actions.onTermsAndConditionsCheckedChanged(it) },
        onLinkClicked = actions.onTermsAndConditionsLinkClicked
    )
    VerticalMargin(Dimension.marginLarge)
    FilledButton(
        text = Label.next,
        enabled = formData.isNextButtonEnabled,
        onClick = actions.onNextButtonClicked
    )
    VerticalMargin(Dimension.marginLarge)
    Spacer(modifier = Modifier.weight(Dimension.weightFull))
    PlainIconButton(
        text = Label.loginScanQrButtonLabel,
        icon = R.drawable.ic_qr,
        color = MaterialTheme.colorScheme.primary,
        onClick = actions.onScanQrButtonClicked
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

@Composable
private fun TermsAndConditionsCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    onLinkClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier
                .padding(Dimension.zero)
                .size(Dimension.checkboxSize),
            checked = checked,
            onCheckedChange = { onCheckedChanged(it) }
        )
        HorizontalMargin(Dimension.marginSmall)
        HyperlinkText(
            modifier = Modifier.weight(1.0f),
            text = Label.loginTermsAndConditionsPromptText,
            links = mapOf(
                Label.loginTermsAndConditionsClickableText to onLinkClicked
            ),
            style = MaterialTheme.typography.bodySmall,
            linkStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline)
        )
    }
}

private data class LoginFormData(
    val isNextButtonEnabled: Boolean,
    val nameText: String,
    val passwordText: String,
    val nameErrorMessage: String?,
    val passwordErrorMessage: String?,
    val termsAndConditionsChecked: Boolean
)

private data class LoginActions(
    val onNameInputChanged: (String) -> Unit,
    val onPasswordInputChanged: (String) -> Unit,
    val onInfoClicked: () -> Unit,
    val onHelpClicked: () -> Unit,
    val onTermsAndConditionsCheckedChanged: (Boolean) -> Unit,
    val onTermsAndConditionsLinkClicked: () -> Unit,
    val onNextButtonClicked: () -> Unit,
    val onScanQrButtonClicked: () -> Unit
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
            formData = LoginFormData(
                isNextButtonEnabled = true,
                nameText = "Wedding123",
                passwordText = "password",
                nameErrorMessage = null,
                passwordErrorMessage = null,
                termsAndConditionsChecked = true
            ),
            isInternetPopupEnabled = false,
            sheetState = sheetState,
            actions = LoginActions(
                onNameInputChanged = {},
                onPasswordInputChanged = {},
                onInfoClicked = {},
                onHelpClicked = {},
                onTermsAndConditionsCheckedChanged = {},
                onTermsAndConditionsLinkClicked = {},
                onNextButtonClicked = {},
                onScanQrButtonClicked = {}
            )
        )
    }
}
