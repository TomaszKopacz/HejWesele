package com.miquido.android.datetimeformatter

import android.content.Context
import com.miquido.android.datetime.Clock
import com.miquido.android.datetime.localDate
import com.miquido.androidtemplate.datetimeformatter.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.math.abs

internal class DateTimeFormatterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clock: Clock
) : DateTimeFormatter {

    override fun format(instant: Instant, format: Format): String {
        return format(instant.toLocalDateTime(clock.zone()), format)
    }

    override fun format(localDate: LocalDate, format: Format): String {
        return format(localDate.atStartOfDayIn(clock.zone()), format)
    }

    override fun format(localDateTime: LocalDateTime, format: Format): String {
        val currentDate = clock.localDate()
        val providedDate = localDateTime.date
        val daysBetween = currentDate.daysUntil(providedDate)

        val formatsDate = format.currentYear != null && format.otherYear != null
        val formatsTime = format.time != null
        val isCloseDate = format.closeDateRange != null && abs(daysBetween) <= format.closeDateRange
        val isCurrentYear = providedDate.year == currentDate.year

        val formattedDate: String? = when {
            formatsDate && isCloseDate -> formatCloseDate(daysBetween)
            formatsDate && isCurrentYear ->
                java.time.format.DateTimeFormatter
                    .ofPattern(format.currentYear!!.patternString)
                    .format(localDateTime.toJavaLocalDateTime())
            formatsDate ->
                java.time.format.DateTimeFormatter
                    .ofPattern(format.otherYear!!.patternString)
                    .format(localDateTime.toJavaLocalDateTime())
            else -> null
        }

        val formattedTime: String? = if (formatsTime) {
            java.time.format.DateTimeFormatter
                .ofPattern(format.time!!.patternString)
                .format(localDateTime.toJavaLocalDateTime())
        } else {
            null
        }

        return listOfNotNull(formattedDate, formattedTime)
            .joinToString(separator = format.dateTimeSeparator)
    }

    private fun formatCloseDate(daysBetween: Int): String {
        val yesterday = R.string.date_format_yesterday
        val today = R.string.date_format_today
        val tomorrow = R.string.date_format_tomorrow
        val closePast = R.string.date_format_close_past
        val closeFuture = R.string.date_format_close_future

        return context.resources.getString(
            when {
                daysBetween == -1 -> yesterday
                daysBetween == 0 -> today
                daysBetween == 1 -> tomorrow
                daysBetween < 0 -> closePast
                else -> closeFuture
            },
            abs(daysBetween)
        )
    }
}
