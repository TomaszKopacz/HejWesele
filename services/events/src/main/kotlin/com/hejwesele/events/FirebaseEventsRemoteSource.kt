package com.hejwesele.events

import com.hejwesele.events.dto.EventDto
import com.hejwesele.events.mappers.mapDto
import com.hejwesele.events.mappers.safeMapModel
import com.hejwesele.events.model.Event
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabase
import com.hejwesele.realtimedatabase.FirebaseResult.Error
import com.hejwesele.realtimedatabase.FirebaseResult.NoSuchItem
import com.hejwesele.realtimedatabase.FirebaseResult.Success
import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result
import com.hejwesele.result.completed
import com.hejwesele.result.failed
import com.hejwesele.result.failure
import com.hejwesele.result.flatMapSuccess
import com.hejwesele.result.notFoundError
import com.hejwesele.result.serviceError
import com.hejwesele.result.success
import javax.inject.Inject

class FirebaseEventsRemoteSource @Inject constructor(
    private val database: FirebaseRealtimeDatabase
) : EventsRemoteSource {

    companion object {
        private const val EVENTS_PATH = "events/"
    }

    override suspend fun getEvent(eventId: String): Result<Event> {
        val result = database.read(EVENTS_PATH, eventId, EventDto::class)

        return when (result) {
            is Success -> success(result.value)
            is Error -> failure(serviceError(result.exception))
            is NoSuchItem -> failure(notFoundError())
        }.flatMapSuccess { dto -> dto.safeMapModel() }
    }

    override suspend fun addEvent(event: Event): CompletableResult {
        val eventSaved = database.write(
            path = EVENTS_PATH,
            item = event.mapDto()
        )

        return if (eventSaved) completed() else failed(serviceError())
    }
}
