package com.hejwesele.events.model

data class EventSettings(
    val id: String,
    val name: String,
    val detailsId: String,
    val invitationId: String?,
    val scheduleId: String?,
    val servicesId: String?,
    val galleryId: String?,
    val galleryHintDismissed: Boolean
)
