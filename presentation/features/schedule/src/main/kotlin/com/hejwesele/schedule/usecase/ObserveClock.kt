package com.hejwesele.schedule.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Duration

internal class ObserveClock @Inject constructor() {

    suspend operator fun invoke(delay: Duration) = withContext(Dispatchers.Default) {
        flow {
            while (true) {
                emit(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
                delay(delay)
            }
        }
    }
}
