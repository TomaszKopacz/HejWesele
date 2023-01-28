package com.hejwesele.home.model

import com.hejwesele.model.home.HomeTileType

internal data class HomeTileUiModel(
    val type: HomeTileType,
    val title: String,
    val subtitle: String?,
    val description: String,
    val photoUrls: List<String>
)
