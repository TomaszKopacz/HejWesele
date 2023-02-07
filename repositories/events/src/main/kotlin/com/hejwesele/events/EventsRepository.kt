package com.hejwesele.events

import com.hejwesele.events.model.Event
import com.hejwesele.events.model.Events
import com.hejwesele.result.Result
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val remoteSource: EventsRemoteSource
) {

    suspend fun getEvents(): Result<Events> {
        return remoteSource.getEvents()
    }

    suspend fun getEvent(id: String): Result<Event> {
        return remoteSource.getEvent(id)
    }

    suspend fun addEvent(event: Event) {
        remoteSource.addEvent(event)
    }
}
