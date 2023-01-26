package com.hejwesele.datastore

import com.hejwesele.datastore.dto.event.EventDto
import com.hejwesele.datastore.dto.home.HomeTileDto
import kotlinx.coroutines.flow.SharedFlow

interface Datastore {

    suspend fun getEvent(eventId: String): DatastoreResult<EventDto>

    suspend fun getAllEvents(): DatastoreResult<List<EventDto>>

    suspend fun getHomeTiles(eventId: String): DatastoreResult<List<HomeTileDto>>

    suspend fun observeEvent(eventId: String): SharedFlow<EventDto>
}
