package com.hejwesele.events

import com.hejwesele.events.model.Event
import com.hejwesele.result.Result
import javax.inject.Inject

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
