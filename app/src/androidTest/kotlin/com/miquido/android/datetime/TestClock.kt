package com.miquido.android.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

object TestClock : Clock {
    lateinit var now: Instant
    lateinit var zone: TimeZone

    override fun now() = now
    override fun zone() = zone
}
