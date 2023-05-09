package com.hejwesele.schedules

import com.hejwesele.remotedatabase.RemoteDatabase
import com.hejwesele.result.notFound
import com.hejwesele.schedules.dto.ScheduleDto
import com.hejwesele.schedules.mappers.mapDto
import com.hejwesele.schedules.mappers.mapModel
import com.hejwesele.schedules.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatabaseSchedulesRemoteSource @Inject constructor(
    private val database: RemoteDatabase
) : SchedulesRemoteSource {

    companion object {
        private const val SCHEDULES_PATH = "schedules/"
    }

    override suspend fun observeSchedule(scheduleId: String): Flow<Result<Schedule>> = withContext(Dispatchers.IO) {
        database.observe(
            path = SCHEDULES_PATH,
            id = scheduleId,
            type = ScheduleDto::class
        ).map { result ->
            result.mapCatching { dto -> dto.mapModel() }
        }
    }

    override suspend fun getSchedule(scheduleId: String): Result<Schedule> = withContext(Dispatchers.IO) {
        database.read(
            path = SCHEDULES_PATH,
            id = scheduleId,
            type = ScheduleDto::class
        ).mapCatching { dto ->
            dto?.mapModel() ?: throw notFound(name = "schedule", id = scheduleId)
        }
    }

    override suspend fun addSchedule(schedule: Schedule): Result<Schedule> = withContext(Dispatchers.IO) {
        database.write(
            path = SCHEDULES_PATH,
            item = schedule.mapDto()
        ).map { schedule }
    }
}
