package com.hejwesele.settings.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class EventDto(
    val eventId: String? = null,
    val eventName: String? = null,
    val date: String? = null,
    val invitationId: String? = null,
    val galleryId: String? = null
)
