package com.hejwesele.android.components

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

@Composable
fun ErrorView(
    title: String = Label.errorTitle,
    description: String = Label.errorDescription,
    onRetry: () -> Unit
) {
    ScrollableColumn {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background
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
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalMargin(Dimension.marginNormal)
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalMargin(Dimension.marginNormal)
                    PlainButton(
                        text = Label.retry,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleSmall,
                        onClick = onRetry,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ErrorViewPreview() {
    AppTheme(darkTheme = false) {
        ErrorView(onRetry = {})
    }
}

@Composable
fun ErrorDialog(
    title: String = Label.errorTitle,
    description: String = Label.errorDescription,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
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
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalMargin(Dimension.marginNormal)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalMargin(Dimension.marginNormal)
                PlainButton(
                    text = Label.ok,
                    color = MaterialTheme.colorScheme.error,
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                )
                VerticalMargin(Dimension.marginNormal)
            }
        }
    }
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    AppTheme(darkTheme = false) {
        ErrorDialog(onDismiss = {})
    }
}
