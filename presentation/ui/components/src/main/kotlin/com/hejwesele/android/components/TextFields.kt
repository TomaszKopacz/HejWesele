package com.hejwesele.android.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Transitions.fadeIn
import com.hejwesele.android.theme.Transitions.fadeOut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors() = with(MaterialTheme.colorScheme) {
    TextFieldDefaults.textFieldColors(
        textColor = this.onSurface,
        containerColor = Color.Transparent,
        cursorColor = primary,
        focusedIndicatorColor = primary,
        unfocusedIndicatorColor = outline,
        focusedLabelColor = outline,
        unfocusedLabelColor = outline,
        errorIndicatorColor = error
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    label: String? = null,
    transformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Default,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = "",
    onTextChanged: (String) -> Unit
) {
    var input by remember { mutableStateOf(text) }

    Box(modifier = modifier) {
        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = input,
                label = label?.let { { Text(text = it) } },
                enabled = enabled,
                isError = isError,
                textStyle = MaterialTheme.typography.bodyLarge,
                shape = MaterialTheme.shapes.extraLarge,
                colors = textFieldColors(),
                visualTransformation = transformation,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
                onValueChange = {
                    onTextChanged(it)
                    input = it
                }
            )
            AnimatedVisibility(
                visible = !errorMessage.isNullOrEmpty(),
                enter = fadeIn,
                exit = fadeOut
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = Dimension.marginSmall,
                        vertical = Dimension.marginExtraSmall2X
                    ),
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun FormTextFieldPreview() {
    AppTheme(darkTheme = false) {
        FormTextField(
            text = "Text",
            label = "Label",
            isError = false,
            errorMessage = null,
            onTextChanged = {}
        )
    }
}
