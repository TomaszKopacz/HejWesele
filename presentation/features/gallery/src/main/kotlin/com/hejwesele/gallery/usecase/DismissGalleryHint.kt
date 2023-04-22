package com.hejwesele.gallery.usecase

import com.hejwesele.events.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DismissGalleryHint @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository.setGalleryHintDismissed(true)
    }
}
