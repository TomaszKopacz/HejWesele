package com.hejwesele.repository

import com.hejwesele.model.DataResult
import com.hejwesele.model.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface IEventRepository {

    suspend fun getEvent(eventId: String): DataResult<Event>

    suspend fun observeEvent(eventId: String, coroutineScope: CoroutineScope): SharedFlow<Event>
}
