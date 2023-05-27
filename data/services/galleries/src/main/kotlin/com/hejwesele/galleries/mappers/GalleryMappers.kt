package com.hejwesele.galleries.mappers

import com.hejwesele.galleries.dto.GalleryDto
import com.hejwesele.galleries.model.Gallery

internal fun GalleryDto.mapModel() = Gallery(
    externalGallery = externalGallery,
    photos = photos ?: emptyList()
)

internal fun Gallery.mapDto(): GalleryDto {
    return GalleryDto(
        externalGallery = externalGallery,
        photos = photos
    )
}
