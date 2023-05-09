package com.hejwesele.schedules.dto

import androidx.annotation.Keep

@Keep
data class ScheduleDto(
    val activities: List<ScheduleActivityDto> = emptyList()
)
