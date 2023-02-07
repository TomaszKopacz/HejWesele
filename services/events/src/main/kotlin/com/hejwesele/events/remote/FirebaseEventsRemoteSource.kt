package com.hejwesele.events.remote

import com.hejwesele.events.EventsRemoteSource
import com.hejwesele.events.model.Event
import com.hejwesele.events.model.Events
import com.hejwesele.events.remote.dto.EventDto
import com.hejwesele.events.remote.dto.EventsDto
import com.hejwesele.events.remote.mappers.toDto
import com.hejwesele.events.remote.mappers.toModel
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabase
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabaseResult.Error
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabaseResult.Success
import com.hejwesele.result.Result
import com.hejwesele.result.failure
import com.hejwesele.result.serviceError
import com.hejwesele.result.success
import javax.inject.Inject

class FirebaseEventsRemoteSource @Inject constructor(
    private val database: FirebaseRealtimeDatabase
) : EventsRemoteSource {

    companion object {
        private const val EVENTS_PATH = "events/"
        private const val EVENTS_ITEMS_PATH = "events/data/"
    }

    override suspend fun getEvents(): Result<Events> {
        return when (val result = database.read(EVENTS_PATH, EventsDto::class)) {
            is Success -> success(result.value.toModel())
            is Error -> failure(serviceError(exception = result.exception))
        }
    }

    override suspend fun getEvent(id: String): Result<Event> {
        return when (val result = database.read("$EVENTS_ITEMS_PATH/$id", EventDto::class)) {
            is Success -> success(result.value.toModel())
            is Error -> failure(serviceError(exception = result.exception))
        }
    }

    override suspend fun addEvent(event: Event) {
        database.write(
            path = EVENTS_ITEMS_PATH,
            item = event.toDto()
        )
    }
}
