package com.hejwesele.home.model

import androidx.annotation.DrawableRes

data class IntentUiModel(
    val title: String,
    @DrawableRes val iconResId: Int,
    val intentPackage: String?,
    val url: String
)
