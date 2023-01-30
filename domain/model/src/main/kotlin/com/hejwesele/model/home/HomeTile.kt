package com.hejwesele.model.home

import com.hejwesele.model.common.IntentUrl

data class HomeTile(
    val type: HomeTileType,
    val title: String,
    val subtitle: String?,
    val description: String,
    val photoUrls: List<String>,
    val intents: List<IntentUrl>?
)

enum class HomeTileType {
    COUPLE,
    DATE,
    CHURCH,
    VENUE,
    WISHES
}
