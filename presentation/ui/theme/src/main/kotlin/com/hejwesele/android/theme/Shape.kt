package com.hejwesele.android.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

internal val Shapes = Shapes(
    extraSmall = RoundedCornerShape(Dimension.radiusRoundedCornerExtraLarge),
    small = RoundedCornerShape(Dimension.radiusRoundedCornerLarge),
    medium = RoundedCornerShape(Dimension.radiusRoundedCornerMedium),
    large = RoundedCornerShape(Dimension.radiusRoundedCornerSmall),
    extraLarge = RoundedCornerShape(Dimension.radiusRoundedCornerExtraSmall)
)
