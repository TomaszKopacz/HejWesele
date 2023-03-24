package com.hejwesele.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.hejwesele.android.components.FilledButton
import com.hejwesele.android.components.FormTextField
import com.hejwesele.android.components.PlainIconButton
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.Dimension
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun Login(navigation: ILoginFeatureNavigation) {
    LoginScreen(navigation)
}

@Suppress("MagicNumber")
@Composable
private fun LoginScreen(
    navigation: ILoginFeatureNavigation
) {
    var errorMessage: String? by remember { mutableStateOf(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimension.marginLarge),
        contentAlignment = Alignment.Center
    ) {
        Column {
            FormTextField(
                text = "",
                label = "Nazwa",
                imeAction = ImeAction.Next,
                isError = errorMessage != null,
                errorMessage = errorMessage,
                onTextChanged = { value ->
                    errorMessage = if (value.length > 5) "Max 5 characters!" else null
                }
            )
            VerticalMargin(height = Dimension.marginLarge)
            FormTextField(
                text = "******",
                label = "Hasło",
                transformation = PasswordVisualTransformation(),
                enabled = false,
                onTextChanged = {}
            )
            VerticalMargin(height = Dimension.marginLarge)
            FormTextField(
                text = "",
                label = "Błąd",
                imeAction = ImeAction.Done,
                isError = true,
                errorMessage = "Wystąpił błąd",
                onTextChanged = {}
            )
            VerticalMargin(height = Dimension.marginLarge)
            FilledButton(
                text = "DALEJ",
                onClick = { navigation.openEvent() }
            )
            VerticalMargin(height = Dimension.marginLarge)
            PlainIconButton(
                text = "SKANUJ QR",
                icon = R.drawable.ic_qr,
                color = MaterialTheme.colorScheme.primary,
                onClick = {}
            )
        }
    }
}
