package com.hejwesele.schedule.usecase

import com.hejwesele.schedules.SchedulesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ObserveSchedule @Inject constructor(
    private val repository: SchedulesRepository
) {

    suspend operator fun invoke(scheduleId: String) = withContext(Dispatchers.IO) {
        repository.observeSchedule(scheduleId)
    }
}
