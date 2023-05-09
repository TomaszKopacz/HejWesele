package com.hejwesele.events.model

data class Event(
    val id: String,
    val name: String,
    val password: String,
    val detailsId: String,
    val invitationId: String?,
    val scheduleId: String?,
    val galleryId: String?
)
