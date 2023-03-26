package com.hejwesele.events.model

import kotlinx.datetime.LocalDateTime

data class EventSettings(
    val id: String,
    val name: String,
    val date: LocalDateTime,
    val invitationId: String?,
    val galleryId: String?,
    val galleryHintDismissed: Boolean
)
