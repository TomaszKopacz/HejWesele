package com.hejwesele.events.model

import kotlinx.datetime.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val date: LocalDateTime,
    val invitationId: String?,
    val galleryId: String?
)
