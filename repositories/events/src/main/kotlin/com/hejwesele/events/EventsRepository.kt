package com.hejwesele.events

import com.hejwesele.events.model.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val remoteSource: EventsRemoteSource
) {

    suspend fun getEvent(eventId: String): Result<Event> {
        return remoteSource.getEvent(eventId)
    }

    suspend fun addEvent(event: Event) {
        remoteSource.addEvent(event)
    }
}
