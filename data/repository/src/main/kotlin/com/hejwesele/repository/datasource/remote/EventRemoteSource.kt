package com.hejwesele.repository.datasource.remote

import com.hejwesele.datastore.Datastore
import com.hejwesele.datastore.mapSuccess
import com.hejwesele.model.DataResult
import com.hejwesele.model.event.Event
import com.hejwesele.repository.mapper.toDataResult
import com.hejwesele.repository.mapper.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

internal class EventRemoteSource @Inject constructor(
    private val datastore: Datastore
) {

    suspend fun getEvent(eventId: String): DataResult<Event> {
        return datastore.getEvent(eventId)
            .mapSuccess { it.toModel() }
            .toDataResult()
    }

    suspend fun observeEvent(eventId: String, coroutineScope: CoroutineScope): SharedFlow<Event> {
        return datastore.observeEvent(eventId)
            .map { it.toModel() }
            .shareIn(coroutineScope, SharingStarted.Eagerly)
    }
}
