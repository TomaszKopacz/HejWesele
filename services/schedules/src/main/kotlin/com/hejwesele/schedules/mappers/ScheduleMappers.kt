package com.hejwesele.schedules.mappers

import com.hejwesele.schedules.dto.ScheduleActivityDto
import com.hejwesele.schedules.dto.ScheduleDto
import com.hejwesele.schedules.model.Schedule
import com.hejwesele.schedules.model.ScheduleActivity
import com.hejwesele.schedules.model.ScheduleActivityType.ATTRACTION
import com.hejwesele.schedules.model.ScheduleActivityType.CHURCH
import com.hejwesele.schedules.model.ScheduleActivityType.MEAL
import com.hejwesele.schedules.model.ScheduleActivityType.PARTY
import kotlinx.datetime.toLocalDateTime

internal fun ScheduleDto.mapModel() = Schedule(
    activities = activities.map { it.mapModel() }
)

internal fun ScheduleActivityDto.mapModel() = ScheduleActivity(
    startDate = start?.toLocalDateTime() ?: throw IllegalArgumentException("Required schedule activity start date is not present."),
    endDate = end?.toLocalDateTime() ?: throw IllegalArgumentException("Required schedule activity end date is not present."),
    title = title ?: throw IllegalArgumentException("Required schedule activity title is not present."),
    description = description,
    type = type.mapScheduleActivityTypeModel()
)

internal fun Schedule.mapDto() = ScheduleDto(
    activities = activities.map { it.mapDto() }
)

internal fun ScheduleActivity.mapDto(): ScheduleActivityDto {
    return ScheduleActivityDto(
        start = startDate.toString(),
        end = endDate.toString(),
        title = title,
        description = description
    )
}

internal fun String?.mapScheduleActivityTypeModel() = when (this) {
    "church" -> CHURCH
    "meal" -> MEAL
    "attraction" -> ATTRACTION
    "party" -> PARTY
    else -> ATTRACTION
}
