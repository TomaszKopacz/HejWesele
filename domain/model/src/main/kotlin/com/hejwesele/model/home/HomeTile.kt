package com.hejwesele.model.home

data class HomeTile(
    val type: HomeTileType,
    val title: String,
    val subtitle: String?,
    val description: String,
    val photoUrls: List<String>
)

enum class HomeTileType {
    COUPLE,
    DATE,
    CHURCH,
    VENUE,
    WISHES
}
