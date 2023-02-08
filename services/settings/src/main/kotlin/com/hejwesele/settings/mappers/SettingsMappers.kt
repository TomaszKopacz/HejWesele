package com.hejwesele.settings.mappers

import com.hejwesele.result.Result
import com.hejwesele.result.failure
import com.hejwesele.result.serviceError
import com.hejwesele.result.success
import com.hejwesele.settings.dto.EventDto
import com.hejwesele.settings.dto.EventSettingsDto
import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings
import kotlinx.datetime.toLocalDateTime

internal fun EventSettingsDto.safeMapModel(): Result<EventSettings> {
    return try {
        val settings = EventSettings(
            event = event?.mapModel(),
            galleryHintDismissed = galleryHintDismissed
        )
        success(settings)
    } catch (exception: IllegalArgumentException) {
        failure(serviceError(exception))
    }
}

internal fun Event.mapDto() = EventDto(
    eventId = eventId,
    eventName = eventName,
    date = date.toString(),
    invitationId = invitationId,
    galleryId = galleryId
)

private fun EventDto.mapModel() = Event(
    eventId = eventId ?: throw IllegalArgumentException(),
    eventName = eventName ?: throw IllegalArgumentException(),
    date = date?.toLocalDateTime() ?: throw IllegalArgumentException(),
    invitationId = invitationId,
    galleryId = galleryId
)
