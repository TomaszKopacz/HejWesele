package com.miquido.android.datetimeformatter

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface DateTimeFormatter {
    fun format(instant: Instant, format: Format): String
    fun format(localDate: LocalDate, format: Format): String
    fun format(localDateTime: LocalDateTime, format: Format): String
}
