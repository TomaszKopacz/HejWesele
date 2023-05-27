package com.hejwesele.galleries.dto

import androidx.annotation.Keep

@Keep
data class GalleryDto(
    val externalGallery: String? = null,
    val photos: List<String>? = null
)
