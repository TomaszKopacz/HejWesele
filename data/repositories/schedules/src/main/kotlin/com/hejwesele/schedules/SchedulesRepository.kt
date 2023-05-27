package com.hejwesele.schedules

import com.hejwesele.schedules.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchedulesRepository @Inject constructor(
    private val remoteSource: SchedulesRemoteSource
) {

    suspend fun observeSchedule(scheduleId: String) = withContext(Dispatchers.IO) {
        remoteSource.observeSchedule(scheduleId)
    }

    suspend fun getSchedule(scheduleId: String) = withContext(Dispatchers.IO) {
        remoteSource.getSchedule(scheduleId)
    }

    suspend fun addSchedule(schedule: Schedule) = withContext(Dispatchers.IO) {
        remoteSource.addSchedule(schedule)
    }
}
