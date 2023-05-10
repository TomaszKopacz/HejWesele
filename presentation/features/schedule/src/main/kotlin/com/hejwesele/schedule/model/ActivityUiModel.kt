package com.hejwesele.schedule.model

import androidx.annotation.DrawableRes

data class ActivityUiModel(
    val time: String,
    val title: String,
    val description: String? = null,
    @DrawableRes val typeIconResId: Int
)
