package com.hejwesele.repository

import com.hejwesele.model.DataResult
import com.hejwesele.model.event.Event
import com.hejwesele.model.event.Events
import com.hejwesele.repository.datasource.remote.NewEventRemoteSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class EventRepository @Inject constructor(
    private val remoteSource: NewEventRemoteSource
) : IEventRepository {

    override suspend fun getEvents(path: String): DataResult<Events> {
        return remoteSource.getEvents(path)
    }

    override suspend fun observeEvent(eventId: String, coroutineScope: CoroutineScope): SharedFlow<Event> {
        return remoteSource.observeEvent(eventId, coroutineScope)
    }
}
