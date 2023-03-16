package com.hejwesele.events

import com.hejwesele.events.dto.EventDto
import com.hejwesele.events.mappers.mapDto
import com.hejwesele.events.mappers.mapModel
import com.hejwesele.events.model.Event
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabase
import com.hejwesele.result.notFound
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseEventsRemoteSource @Inject constructor(
    private val database: FirebaseRealtimeDatabase
) : EventsRemoteSource {

    companion object {
        private const val EVENTS_PATH = "events/"
    }

    override suspend fun getEvent(eventId: String): Result<Event> {
        return database.read(
            path = EVENTS_PATH,
            id = eventId,
            type = EventDto::class
        ).mapCatching { dto ->
            dto?.mapModel() ?: throw notFound(name = "event", id = eventId)
        }
    }

    override suspend fun addEvent(event: Event): Result<Event> {
        return database.write(
            path = EVENTS_PATH,
            item = event.mapDto()
        ).map { event }
    }
}
