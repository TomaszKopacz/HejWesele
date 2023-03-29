package com.hejwesele.android.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun ContinuousLottieAnimation(
    modifier: Modifier = Modifier,
    @RawRes animationResId: Int,
    size: Dp,
    aspectRatio: Float = 1.0f
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animationResId))
    LottieAnimation(
        modifier = modifier
            .size(size)
            .aspectRatio(aspectRatio),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}
