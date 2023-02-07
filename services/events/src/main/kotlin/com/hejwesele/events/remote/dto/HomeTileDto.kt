package com.hejwesele.events.remote.dto

import androidx.annotation.Keep

@Keep
data class HomeTileDto(
    var type: String = "",
    var title: String = "",
    var subtitle: String? = null,
    var description: String = "",
    var photo_urls: List<String> = emptyList(),
    var urls: List<String>? = null
)
