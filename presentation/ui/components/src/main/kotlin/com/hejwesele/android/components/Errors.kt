package com.hejwesele.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hejwesele.android.components.layouts.ScrollableColumn
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.components.R

data class ErrorData(
    val title: String,
    val description: String,
    val onRetry: () -> Unit
) {
    companion object {
        val Default = ErrorData(
            title = Label.errorTitleText,
            description = Label.errorDescriptionText,
            onRetry = {}
        )
    }
}

data class AlertData(
    val title: String,
    val description: String,
    val onDismiss: () -> Unit
) {
    companion object {
        val Default = AlertData(
            title = Label.errorTitleText,
            description = Label.errorDescriptionText,
            onDismiss = {}
        )
    }
}

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    data: ErrorData = ErrorData.Default
) {
    ScrollableColumn(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        arrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(Dimension.marginLarge),
                verticalArrangement = Arrangement.Center
            ) {
                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_error))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .padding(top = Dimension.marginLarge)
                        .aspectRatio(1.0f)
                )
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalMargin(Dimension.marginNormal)
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalMargin(Dimension.marginNormal)
                PlainButton(
                    text = Label.retry,
                    color = MaterialTheme.colorScheme.error,
                    onClick = { data.onRetry() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    data: AlertData = AlertData.Default
) {
    Dialog(
        onDismissRequest = data.onDismiss
    ) {
        Surface(
            modifier = modifier,
            elevation = Dimension.elevationSmall,
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(horizontal = Dimension.marginLarge),
                verticalArrangement = Arrangement.Center
            ) {
                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_error))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .padding(top = Dimension.marginLarge)
                        .aspectRatio(1.0f)
                )
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalMargin(Dimension.marginNormal)
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalMargin(Dimension.marginNormal)
                PlainButton(
                    modifier = Modifier.align(Alignment.End),
                    text = Label.ok,
                    color = MaterialTheme.colorScheme.error,
                    size = PlainButtonSize.LARGE,
                    onClick = data.onDismiss
                )
                VerticalMargin(Dimension.marginNormal)
            }
        }
    }
}

@Preview
@Composable
private fun ErrorViewPreview() {
    AppTheme(darkTheme = false) {
        ErrorView()
    }
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    AppTheme(darkTheme = false) {
        AlertDialog()
    }
}
