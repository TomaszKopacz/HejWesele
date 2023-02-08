package com.hejwesele.events

import com.hejwesele.events.model.Event
import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result

interface EventsRemoteSource {
    suspend fun getEvent(eventId: String): Result<Event>

    suspend fun addEvent(event: Event): CompletableResult
}
