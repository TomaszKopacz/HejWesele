package com.hejwesele.galleries.mappers

import com.hejwesele.galleries.dto.GalleryDto
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.result.Result
import com.hejwesele.result.failure
import com.hejwesele.result.serviceError
import com.hejwesele.result.success

internal fun GalleryDto.safeMapModel(): Result<Gallery> {
    return try {
        val gallery = Gallery(
            externalGallery = externalGallery,
            photos = photos ?: throw IllegalArgumentException()
        )
        success(gallery)
    } catch (exception: IllegalArgumentException) {
        failure(serviceError(exception))
    }
}

internal fun Gallery.mapDto(): GalleryDto {
    return GalleryDto(
        externalGallery = externalGallery,
        photos = photos
    )
}
