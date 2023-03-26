package com.hejwesele.events.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class EventSettingsDto(
    val id: String? = null,
    val name: String? = null,
    val date: String? = null,
    val invitationId: String? = null,
    val galleryId: String? = null,
    val galleryHintDismissed: Boolean = false
)
