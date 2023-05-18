package com.hejwesele.events.mappers

import com.hejwesele.events.dto.EventDto
import com.hejwesele.events.dto.EventSettingsDto
import com.hejwesele.events.model.Event
import com.hejwesele.events.model.EventSettings

internal fun EventDto.mapModel() = Event(
    id = id ?: throw IllegalArgumentException("Required event ID is not present."),
    name = name ?: throw IllegalArgumentException("Required event name is not present."),
    password = password ?: throw IllegalArgumentException("Required event password is not present."),
    detailsId = detailsId ?: throw IllegalArgumentException("Required event details are not present."),
    invitationId = invitationId,
    scheduleId = scheduleId,
    servicesId = servicesId,
    galleryId = galleryId
)

internal fun List<EventDto>.mapModel(): List<Event> = map { it.mapModel() }

internal fun Event.mapDto() = EventDto(
    id = id,
    name = name,
    password = password,
    detailsId = detailsId,
    invitationId = invitationId,
    scheduleId = scheduleId,
    galleryId = galleryId
)

internal fun EventSettings.mapDto() = EventSettingsDto(
    id = id,
    name = name,
    detailsId = detailsId,
    invitationId = invitationId,
    scheduleId = scheduleId,
    servicesId = servicesId,
    galleryId = galleryId,
    galleryHintDismissed = galleryHintDismissed
)

internal fun EventSettingsDto.mapModel() = EventSettings(
    id = id ?: throw IllegalArgumentException("Required event ID is not present."),
    name = name ?: throw IllegalArgumentException("Required event name is not present."),
    detailsId = detailsId ?: throw IllegalArgumentException("Required event details are not present."),
    invitationId = invitationId,
    scheduleId = scheduleId,
    servicesId = servicesId,
    galleryId = galleryId,
    galleryHintDismissed = galleryHintDismissed
)
