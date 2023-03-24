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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
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
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun Login(navigation: ILoginFeatureNavigation) {
    LoginEntryPoint(navigation)
}

@Composable
private fun LoginEntryPoint(navigation: ILoginFeatureNavigation) {
    LoginScreen(
        isInternetPopupEnabled = true,
        onNextButtonClick = { navigation.openEvent() }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Suppress("MagicNumber")
@Composable
private fun LoginScreen(
    isInternetPopupEnabled: Boolean,
    onNextButtonClick: () -> Unit
) {
    var errorMessage: String? by remember { mutableStateOf(null) }

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
                Spacer(modifier = Modifier.weight(0.5f))
                NameTextField(
                    errorMessage = errorMessage,
                    onTextChanged = { text ->
                        errorMessage = if (text.length > 5) Label.loginEventNameErrorMessage else null
                    }
                )
                VerticalMargin(Dimension.marginExtraSmall)
                PasswordTextField()
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
                Spacer(modifier = Modifier.weight(1.0f))
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
        onTextChanged = { value -> onTextChanged(value) }
    )
}

@Composable
private fun PasswordTextField() {
    FormTextField(
        label = Label.loginEventPasswordLabel,
        imeAction = ImeAction.Done,
        transformation = PasswordVisualTransformation(),
        onTextChanged = {}
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
            isInternetPopupEnabled = false,
            onNextButtonClick = {}
        )
    }
}
