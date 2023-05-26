package com.hejwesele.cms

import com.hejwesele.schedules.SchedulesRepository
import com.hejwesele.schedules.model.Schedule
import com.hejwesele.schedules.model.ScheduleActivity
import com.hejwesele.schedules.model.ScheduleActivityType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class AddSchedule @Inject constructor(
    private val repository: SchedulesRepository
) {

    @Suppress("LongMethod")
    suspend operator fun invoke(schedule: Schedule? = null) = withContext(Dispatchers.IO) {
        repository.addSchedule(
            Schedule(
                activities = listOf(
                    ScheduleActivity(
                        startDate = "2024-07-14T13:00".toLocalDateTime(),
                        endDate = "2024-07-14T14:30".toLocalDateTime(),
                        title = "Ceremonia ślubna w kościele",
                        description = null,
                        type = ScheduleActivityType.CHURCH
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T14:30".toLocalDateTime(),
                        endDate = "2024-07-14T15:00".toLocalDateTime(),
                        title = "Przywitanie gości w domu weselnym",
                        description = null,
                        type = ScheduleActivityType.ATTRACTION
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T15:00".toLocalDateTime(),
                        endDate = "2024-07-14T16:00".toLocalDateTime(),
                        title = "Posiłek 1",
                        description = "Rosołek + kotlet schabowy z zmieniakami",
                        type = ScheduleActivityType.MEAL
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T16:00".toLocalDateTime(),
                        endDate = "2024-07-14T16:30".toLocalDateTime(),
                        title = "Pierwszy taniec",
                        description = null,
                        type = ScheduleActivityType.ATTRACTION
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T16:30".toLocalDateTime(),
                        endDate = "2024-07-14T18:00".toLocalDateTime(),
                        title = "Zabawa weselna",
                        description = null,
                        type = ScheduleActivityType.PARTY
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T18:00".toLocalDateTime(),
                        endDate = "2024-07-14T18:30".toLocalDateTime(),
                        title = "Posiłek 2",
                        description = "Rosołek + kotlet schabowy z zmieniakami",
                        type = ScheduleActivityType.MEAL
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T18:30".toLocalDateTime(),
                        endDate = "2024-07-14T19:00".toLocalDateTime(),
                        title = "Sesja zdjęciowa",
                        description = null,
                        type = ScheduleActivityType.ATTRACTION
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T19:00".toLocalDateTime(),
                        endDate = "2024-07-14T20:00".toLocalDateTime(),
                        title = "Zabawa weselna",
                        description = null,
                        type = ScheduleActivityType.PARTY
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T20:00".toLocalDateTime(),
                        endDate = "2024-07-14T20:15".toLocalDateTime(),
                        title = "Podziękowania dla rodziców",
                        description = null,
                        type = ScheduleActivityType.ATTRACTION
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T20:15".toLocalDateTime(),
                        endDate = "2024-07-14T21:00".toLocalDateTime(),
                        title = "Zabawa weselna",
                        description = null,
                        type = ScheduleActivityType.PARTY
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T21:00".toLocalDateTime(),
                        endDate = "2024-07-14T21:30".toLocalDateTime(),
                        title = "Posiłek 3",
                        description = "Rosołek + kotlet schabowy z zmieniakami",
                        type = ScheduleActivityType.MEAL
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-14T21:30".toLocalDateTime(),
                        endDate = "2024-07-15T00:00".toLocalDateTime(),
                        title = "Zabawa weselna",
                        description = null,
                        type = ScheduleActivityType.PARTY
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-15T00:00".toLocalDateTime(),
                        endDate = "2024-07-15T01:00".toLocalDateTime(),
                        title = "Oczepiny",
                        description = null,
                        type = ScheduleActivityType.ATTRACTION
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-15T01:00".toLocalDateTime(),
                        endDate = "2024-07-15T01:30".toLocalDateTime(),
                        title = "Posiłek 4",
                        description = "Rosołek + kotlet schabowy z zmieniakami",
                        type = ScheduleActivityType.MEAL
                    ),
                    ScheduleActivity(
                        startDate = "2024-07-15T01:30".toLocalDateTime(),
                        endDate = "2024-07-15T04:00".toLocalDateTime(),
                        title = "Zabawa weselna",
                        description = null,
                        type = ScheduleActivityType.PARTY
                    )
                )
            )
        )
    }
}
