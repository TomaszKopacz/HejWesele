package com.hejwesele.services.model

import androidx.annotation.DrawableRes

internal data class IntentUiModel(
    @DrawableRes val iconResId: Int,
    val intentPackage: String?,
    val url: String
)
