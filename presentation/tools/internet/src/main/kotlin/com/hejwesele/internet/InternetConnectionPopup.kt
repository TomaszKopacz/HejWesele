package com.hejwesele.internet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

private const val PopupDelay = 2_000L

@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@Composable
fun InternetConnectionPopup(
    modifier: Modifier = Modifier,
    viewModel: InternetConnectionViewModel = hiltViewModel()
) {
    val state by viewModel.connectionState.collectAsState()

    val isConnected = state === InternetConnectionState.AVAILABLE
    var isPopupVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            delay(PopupDelay)
        }
        isPopupVisible = !isConnected
    }

    Column(modifier = modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = isPopupVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            InternetConnectionStatusBox(isConnected = isConnected)
        }
    }
}

@Composable
private fun InternetConnectionStatusBox(
    modifier: Modifier = Modifier,
    isConnected: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor by animateColorAsState(
        if (isConnected) colorScheme.tertiaryContainer else colorScheme.errorContainer
    )
    val textColor = if (isConnected) colorScheme.onTertiaryContainer else colorScheme.onErrorContainer
    val message = if (isConnected) Label.networkReconnectedText else Label.networkDisconnectedText

    Box(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(Dimension.marginNormal)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = message,
                color = textColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
    }
}
