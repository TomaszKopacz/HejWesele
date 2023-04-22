package com.hejwesele.cms

import com.hejwesele.galleries.GalleriesRepository
import com.hejwesele.galleries.model.Gallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddGallery @Inject constructor(
    private val repository: GalleriesRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository.addGallery(
            Gallery(
                externalGallery = "https://external.galery/2",
                photos = listOf(
                    "https://photo/1",
                    "https://photo/2"
                )
            )
        )
    }
}
