package com.hejwesele.settings.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class EventSettingsDto(
    val event: EventDto? = null,
    val galleryHintDismissed: Boolean = false
)
