package com.hejwesele.repository

import com.hejwesele.model.DataResult
import com.hejwesele.model.event.Event
import com.hejwesele.repository.datasource.remote.EventRemoteSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class EventRepository @Inject constructor(
    private val remoteSource: EventRemoteSource
) : IEventRepository {

    override suspend fun getEvent(eventId: String): DataResult<Event> {
        return remoteSource.getEvent(eventId)
    }

    override suspend fun observeEvent(eventId: String, coroutineScope: CoroutineScope): SharedFlow<Event> {
        return remoteSource.observeEvent(eventId, coroutineScope)
    }
}
