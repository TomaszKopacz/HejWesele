package com.hejwesele.events

import com.hejwesele.events.model.Event

interface EventsRemoteSource {

    suspend fun getEvents(): Result<List<Event>>

    suspend fun getEvent(eventId: String): Result<Event>

    suspend fun addEvent(event: Event): Result<Event>
}
