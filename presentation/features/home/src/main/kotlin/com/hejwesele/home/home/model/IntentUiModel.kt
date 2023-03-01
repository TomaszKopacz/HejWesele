package com.hejwesele.home.home.model

import androidx.annotation.DrawableRes

internal data class IntentUiModel(
    val title: String,
    @DrawableRes val iconResId: Int,
    val intentPackage: String?,
    val url: String
)
