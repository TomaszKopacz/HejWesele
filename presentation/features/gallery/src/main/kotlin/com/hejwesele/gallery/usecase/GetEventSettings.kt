package com.hejwesele.gallery.usecase

import com.hejwesele.events.EventsRepository
import com.hejwesele.events.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEventSettings @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(): Result<EventSettings> = withContext(Dispatchers.IO) {
        repository.getStoredEventSettings()
    }
}
