package com.hejwesele.settings.model

import kotlinx.datetime.LocalDateTime

data class Event(
    val eventId: String,
    val eventName: String,
    val date: LocalDateTime,
    val invitationId: String?,
    val galleryId: String?
)
