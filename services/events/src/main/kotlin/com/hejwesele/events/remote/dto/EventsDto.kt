package com.hejwesele.events.remote.dto

import androidx.annotation.Keep

@Keep
data class EventsDto(
    val data: List<EventDto> = emptyList()
)
