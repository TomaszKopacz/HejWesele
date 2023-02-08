package com.hejwesele.events.mappers

import com.hejwesele.events.dto.EventDto
import com.hejwesele.events.model.Event
import com.hejwesele.result.Result
import com.hejwesele.result.failure
import com.hejwesele.result.serviceError
import com.hejwesele.result.success
import kotlinx.datetime.toLocalDateTime

internal fun EventDto.safeMapModel() =
    try {
        success(mapModel())
    } catch (exception: IllegalArgumentException) {
        failure(serviceError(exception))
    }

internal fun List<EventDto>.safeMapModel(): Result<List<Event>> =
    try {
        val events = map { it.mapModel() }
        success(events)
    } catch (exception: IllegalArgumentException) {
        failure(serviceError(exception))
    }

internal fun Event.mapDto() = EventDto(
    id = id,
    name = name,
    date = date.toString(),
    invitationId = invitationId,
    galleryId = galleryId
)

private fun EventDto.mapModel() = Event(
    id = id ?: throw IllegalArgumentException(),
    name = name ?: throw IllegalArgumentException(),
    date = date?.toLocalDateTime() ?: throw IllegalArgumentException(),
    invitationId = invitationId,
    galleryId = galleryId
)
