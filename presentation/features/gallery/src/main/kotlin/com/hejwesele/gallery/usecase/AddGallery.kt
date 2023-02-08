package com.hejwesele.gallery.usecase

import com.hejwesele.galleries.GalleriesRepository
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.result.CompletableResult
import javax.inject.Inject

class AddGallery @Inject constructor(
    private val repository: GalleriesRepository
) {

    suspend operator fun invoke(): CompletableResult = repository.addGallery(
        Gallery(
            externalGallery = "https://external.galery/2",
            photos = listOf(
                "https://photo/1",
                "https://photo/2"
            )
        )
    )
}
