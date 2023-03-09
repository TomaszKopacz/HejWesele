package com.hejwesele.events.mappers

import com.hejwesele.events.dto.EventDto
import com.hejwesele.events.model.Event
import kotlinx.datetime.toLocalDateTime

internal fun EventDto.mapModel() = Event(
    id = id ?: throw IllegalArgumentException("Required event ID is not present."),
    name = name ?: throw IllegalArgumentException("Required event name is not present."),
    date = date?.toLocalDateTime() ?: throw IllegalArgumentException("Required event date is not present."),
    invitationId = invitationId,
    galleryId = galleryId
)

internal fun List<EventDto>.mapModel(): List<Event> = map { it.mapModel() }

internal fun Event.mapDto() = EventDto(
    id = id,
    name = name,
    date = date.toString(),
    invitationId = invitationId,
    galleryId = galleryId
)
