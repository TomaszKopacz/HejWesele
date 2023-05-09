package com.hejwesele.schedules

import com.hejwesele.schedules.model.Schedule
import kotlinx.coroutines.flow.Flow

interface SchedulesRemoteSource {

    suspend fun observeSchedule(scheduleId: String): Flow<Result<Schedule>>

    suspend fun getSchedule(scheduleId: String): Result<Schedule>

    suspend fun addSchedule(schedule: Schedule): Result<Schedule>
}
