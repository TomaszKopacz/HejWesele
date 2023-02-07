package com.hejwesele.events.model

data class HomeTile(
    val type: HomeTileType,
    val title: String,
    val subtitle: String? = null,
    val description: String,
    val photoUrls: List<String>,
    val intents: List<IntentUrl>?
)
