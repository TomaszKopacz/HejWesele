package com.hejwesele.events.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class EventSettingsDto(
    val id: String? = null,
    val name: String? = null,
    val detailsId: String? = null,
    val invitationId: String? = null,
    val scheduleId: String? = null,
    val servicesId: String? = null,
    val galleryId: String? = null,
    val galleryHintDismissed: Boolean = false
)
