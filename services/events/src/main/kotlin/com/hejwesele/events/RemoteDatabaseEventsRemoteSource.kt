package com.hejwesele.events

import com.hejwesele.events.dto.EventDto
import com.hejwesele.events.mappers.mapDto
import com.hejwesele.events.mappers.mapModel
import com.hejwesele.events.model.Event
import com.hejwesele.remotedatabase.RemoteDatabase
import com.hejwesele.result.extensions.flatMap
import com.hejwesele.result.notFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatabaseEventsRemoteSource @Inject constructor(
    private val database: RemoteDatabase
) : EventsRemoteSource {

    companion object {
        private const val EVENTS_PATH = "events/"
    }

    override suspend fun getEvents(): Result<List<Event>> = withContext(Dispatchers.IO) {
        database.readAll(
            path = EVENTS_PATH,
            type = EventDto::class
        ).flatMap { dtos ->
            runCatching { dtos.mapModel() }
        }
    }

    override suspend fun getEvent(eventId: String): Result<Event> = withContext(Dispatchers.IO) {
        database.read(
            path = EVENTS_PATH,
            id = eventId,
            type = EventDto::class
        ).mapCatching { dto ->
            dto?.mapModel() ?: throw notFound(name = "event", id = eventId)
        }
    }

    override suspend fun addEvent(event: Event): Result<Event> = withContext(Dispatchers.IO) {
        database.write(
            path = EVENTS_PATH,
            item = event.mapDto()
        ).map { event }
    }
}
