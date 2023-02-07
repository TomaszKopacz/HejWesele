package com.hejwesele.events

import com.hejwesele.events.model.Event
import com.hejwesele.events.model.Events
import com.hejwesele.result.Result

interface EventsRemoteSource {

    suspend fun getEvents(): Result<Events>

    suspend fun getEvent(id: String): Result<Event>

    suspend fun addEvent(event: Event)
}
