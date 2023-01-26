package com.hejwesele.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.TextBodyLarge
import com.hejwesele.android.theme.Dimension

@Composable
internal fun Home(
    eventId: Int,
    viewModel: HomeViewModel
) {
    val systemUiController = rememberSystemUiController()
    SideEffect { systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true) }

    viewModel.init().also {
        Content(viewModel)
    }

}

@Composable
internal fun Content(viewModel: HomeViewModel) {
    val uiState by viewModel.states.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(
                WindowInsets
                    .statusBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                    .asPaddingValues()
            )
    ) {
        Column(
            modifier = Modifier
                .padding(Dimension.marginLarge)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CoupleLottieAnimation()
            TextBodyLarge(
                text = uiState.eventName
            )
        }
    }
}

@Composable
internal fun CoupleLottieAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_wedding_couple))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.aspectRatio(1f)
    )
}
