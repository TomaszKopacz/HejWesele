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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hejwesele.android.components.TextBodyLarge
import com.hejwesele.android.theme.Dimension

@Composable
fun Home(eventId: Int) {
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
            modifier = Modifier.padding(Dimension.marginLarge).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CoupleLottieAnimation()
            TextBodyLarge(
                text = "Hej Wesele! Id wydarzenia: $eventId"
            )
        }
    }
}

@Composable
fun CoupleLottieAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_wedding_couple))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever, modifier = Modifier.aspectRatio(1f))
}
