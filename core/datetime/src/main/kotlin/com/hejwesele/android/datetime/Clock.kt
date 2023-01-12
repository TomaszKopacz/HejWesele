package com.hejwesele.android.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface Clock {
    fun now(): Instant
    fun zone(): TimeZone
}

fun Clock.localDate(): LocalDate {
    return now().toLocalDateTime(zone()).date
}

fun Clock.localDateTime(): LocalDateTime {
    return now().toLocalDateTime(zone())
}
