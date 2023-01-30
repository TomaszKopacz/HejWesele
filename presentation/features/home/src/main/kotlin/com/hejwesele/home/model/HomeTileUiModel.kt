package com.hejwesele.home.model

import androidx.annotation.RawRes
import com.hejwesele.model.home.HomeTileType

internal data class HomeTileUiModel(
    val type: HomeTileType,
    val title: String,
    val subtitle: String?,
    val description: String,
    val photoUrls: List<String>,
    @RawRes val animationResId: Int,
    val intents: List<IntentUiModel>,
    val clickable: Boolean
)
