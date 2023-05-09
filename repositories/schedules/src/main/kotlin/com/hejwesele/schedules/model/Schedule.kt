package com.hejwesele.schedules.model

import kotlinx.datetime.LocalDateTime

data class Schedule(
    val activities: List<ScheduleActivity>
)

data class ScheduleActivity(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val title: String,
    val description: String?
)
