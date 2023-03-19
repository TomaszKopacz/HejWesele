package com.hejwesele.events

import com.hejwesele.events.model.Event
import com.hejwesele.events.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val remoteSource: EventsRemoteSource,
    private val localSource: EventsLocalSource
) {
    suspend fun getEvent(eventId: String): Result<Event> = withContext(Dispatchers.IO) {
        remoteSource.getEvent(eventId)
    }

    suspend fun addEvent(event: Event) = withContext(Dispatchers.IO) {
        remoteSource.addEvent(event)
    }

    suspend fun storeEvent(event: Event): Result<Event> = withContext(Dispatchers.IO) {
        localSource.setEvent(event)
    }

    suspend fun getStoredEvent(): Result<EventSettings> = withContext(Dispatchers.IO) {
        localSource.getEventSettings()
    }

    suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        localSource.setGalleryHintDismissed(dismissed)
    }
}
