package com.hejwesele.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.FilledButton
import com.hejwesele.android.components.FormTextField
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.PlainIconButton
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.components.layouts.ScrollableColumn
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.extensions.noRippleClickable
import com.hejwesele.internet.InternetConnectionPopup
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun Login(navigation: ILoginFeatureNavigation) {
    LoginEntryPoint(navigation)
}

@Composable
private fun LoginEntryPoint(
    navigation: ILoginFeatureNavigation,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()

    LoginEventHandler(
        uiState = uiState,
        viewModel = viewModel,
        navigation = navigation
    )

    LoginScreen(
        nameErrorMessage = uiState.eventNameError?.message,
        passwordErrorMessage = uiState.eventPasswordError?.message,
        isInternetPopupEnabled = true,
        onNameInputChanged = { text -> viewModel.onNameInputChanged(text) },
        onPasswordInputChanged = { text -> viewModel.onPasswordInputChanged(text) },
        onNextButtonClick = { viewModel.onSubmit() }
    )
}

@Composable
private fun LoginEventHandler(
    uiState: LoginUiState,
    viewModel: LoginViewModel,
    navigation: ILoginFeatureNavigation
) {
    EventEffect(
        event = uiState.openEvent,
        onConsumed = { viewModel.onEventOpened() },
        action = { navigation.openEvent() }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun LoginScreen(
    nameErrorMessage: String?,
    passwordErrorMessage: String?,
    isInternetPopupEnabled: Boolean,
    onNameInputChanged: (String) -> Unit,
    onPasswordInputChanged: (String) -> Unit,
    onNextButtonClick: () -> Unit
) {
    Scaffold { padding ->
        Column {
            if (isInternetPopupEnabled) {
                InternetConnectionPopup()
            }
            VerticalMargin(Dimension.marginNormal)
            ScrollableColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        start = Dimension.marginLarge,
                        end = Dimension.marginLarge,
                        bottom = padding.calculateBottomPadding()
                    )
            ) {
                InfoIconButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .size(Dimension.iconSizeNormal)
                        .noRippleClickable(onClick = {})
                )
                VerticalMargin(Dimension.marginLarge2X)
                LogoIcon(modifier = Modifier.size(Dimension.iconSizeExtraLarge))
                VerticalMargin(Dimension.marginLarge)
                Spacer(modifier = Modifier.weight(Dimension.weightHalf))
                NameTextField(
                    errorMessage = nameErrorMessage,
                    onTextChanged = { text -> onNameInputChanged(text) }
                )
                VerticalMargin(Dimension.marginExtraSmall)
                PasswordTextField(
                    errorMessage = passwordErrorMessage,
                    onTextChanged = { text -> onPasswordInputChanged(text) }
                )
                VerticalMargin(Dimension.marginNormal)
                HelpLabel(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .noRippleClickable {}
                )
                VerticalMargin(Dimension.marginLarge)
                ButtonNext(
                    onClick = onNextButtonClick
                )
                VerticalMargin(Dimension.marginLarge)
                Spacer(modifier = Modifier.weight(Dimension.weightFull))
                ButtonScanQr()
                VerticalMargin(Dimension.marginNormal)
            }
        }
    }
}

@Composable
private fun InfoIconButton(modifier: Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_info),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun LogoIcon(modifier: Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_logo),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun NameTextField(
    errorMessage: String?,
    onTextChanged: (String) -> Unit
) {
    FormTextField(
        label = Label.loginEventNameLabel,
        imeAction = ImeAction.Next,
        isError = errorMessage != null,
        errorMessage = errorMessage,
        onTextChanged = { text -> onTextChanged(text) }
    )
}

@Composable
private fun PasswordTextField(
    errorMessage: String?,
    onTextChanged: (String) -> Unit
) {
    FormTextField(
        label = Label.loginEventPasswordLabel,
        imeAction = ImeAction.Done,
        isError = errorMessage != null,
        errorMessage = errorMessage,
        transformation = PasswordVisualTransformation(),
        onTextChanged = { text -> onTextChanged(text) }
    )
}

@Composable
private fun HelpLabel(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(Dimension.iconSizeSmall),
            painter = painterResource(R.drawable.ic_question),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        HorizontalMargin(Dimension.marginExtraSmall)
        Text(
            text = Label.loginHelpLabel,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ButtonNext(onClick: () -> Unit) {
    FilledButton(
        text = Label.next,
        onClick = onClick
    )
}

@Composable
private fun ButtonScanQr() {
    PlainIconButton(
        text = Label.loginScanQrButtonLabel,
        icon = R.drawable.ic_qr,
        color = MaterialTheme.colorScheme.primary,
        onClick = {}
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppTheme(darkTheme = false) {
        LoginScreen(
            nameErrorMessage = null,
            passwordErrorMessage = null,
            isInternetPopupEnabled = false,
            onNameInputChanged = {},
            onPasswordInputChanged = {},
            onNextButtonClick = {}
        )
    }
}
