package com.hejwesele.events

import com.hejwesele.events.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val remoteSource: EventsRemoteSource
) {

    suspend fun getEvent(eventId: String): Result<Event> = withContext(Dispatchers.IO) {
        remoteSource.getEvent(eventId)
    }

    suspend fun addEvent(event: Event) = withContext(Dispatchers.IO) {
        remoteSource.addEvent(event)
    }
}
