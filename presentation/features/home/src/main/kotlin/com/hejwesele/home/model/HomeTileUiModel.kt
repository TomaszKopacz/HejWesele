package com.hejwesele.home.model

import androidx.annotation.RawRes
import com.hejwesele.events.model.HomeTileType

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
