package com.hejwesele.android.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import javax.inject.Inject

class ClockImpl @Inject constructor() : Clock {

    override fun now(): Instant {
        return kotlinx.datetime.Clock.System.now()
    }

    override fun zone(): TimeZone {
        return TimeZone.currentSystemDefault()
    }
}
