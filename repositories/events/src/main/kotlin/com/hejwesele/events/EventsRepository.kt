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

    suspend fun findEvent(eventName: String): Result<Event?> = withContext(Dispatchers.IO) {
        remoteSource.getEvents()
            .mapCatching { events ->
                events.firstOrNull { event -> event.name == eventName }
            }
    }

    suspend fun addEvent(event: Event) = withContext(Dispatchers.IO) {
        remoteSource.addEvent(event)
    }

    suspend fun storeEventSettings(event: EventSettings): Result<EventSettings> = withContext(Dispatchers.IO) {
        localSource.setEventSettings(event)
    }

    suspend fun getStoredEventSettings(): Result<EventSettings> = withContext(Dispatchers.IO) {
        localSource.getEventSettings()
    }

    suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        localSource.setGalleryHintDismissed(dismissed)
    }
}
