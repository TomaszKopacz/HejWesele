package com.hejwesele.schedules.dto

import androidx.annotation.Keep

@Keep
data class ScheduleActivityDto(
    val start: String? = null,
    val end: String? = null,
    val title: String? = null,
    val description: String? = null,
    val type: String? = null
)
