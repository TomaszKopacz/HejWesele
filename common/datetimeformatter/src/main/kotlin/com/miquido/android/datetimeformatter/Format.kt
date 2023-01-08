package com.miquido.android.datetimeformatter

import com.miquido.android.datetimeformatter.DatePattern.DayMonthLongPattern
import com.miquido.android.datetimeformatter.DatePattern.DayMonthShortPattern
import com.miquido.android.datetimeformatter.DatePattern.DayMonthYearLongPattern
import com.miquido.android.datetimeformatter.DatePattern.DayMonthYearShortPattern
import com.miquido.android.datetimeformatter.TimePattern.HourMinuteSecondTimePattern
import com.miquido.android.datetimeformatter.TimePattern.HourMinuteTimePattern

sealed class Format private constructor(
    val closeDateRange: Int? = null,
    val time: TimePattern? = null,
    val currentYear: DatePattern? = null,
    val otherYear: DatePattern? = null,
    val dateTimeSeparator: String = "\u00A0"
) {

    companion object {
        const val TODAY = 0
        const val DAY = 1
        const val WEEK = 7
        const val MONTH = 30
        const val QUARTER = 90
    }

    init {
        require(time != null || currentYear != null || otherYear != null)
        require((currentYear == null && otherYear == null) || (currentYear != null && otherYear != null))
    }

    private constructor(
        closeDateRange: Int? = null,
        time: TimePattern? = null,
        date: DatePattern? = null,
        dateTimeSeparator: String = "\u00A0"
    ) : this(
        closeDateRange = closeDateRange,
        time = time,
        currentYear = date,
        otherYear = date,
        dateTimeSeparator = dateTimeSeparator
    )

    // 12:34
    object Time : Format(time = HourMinuteTimePattern)

    // 12:34:00
    object TimeWithSeconds : Format(time = HourMinuteSecondTimePattern)

    // 20 Dec / 20 Dec 2021
    class ShortDate(closeDateRange: Int? = null) : Format(
        closeDateRange = closeDateRange,
        currentYear = DayMonthShortPattern,
        otherYear = DayMonthYearShortPattern
    )

    // 20 Dec 2021
    class ShortDateWithYear(closeDateRange: Int? = null) : Format(
        closeDateRange = closeDateRange,
        date = DayMonthYearShortPattern
    )

    // 20 December / 20 December 2021
    class LongDate(closeDateRange: Int? = null) : Format(
        closeDateRange = closeDateRange,
        currentYear = DayMonthLongPattern,
        otherYear = DayMonthYearLongPattern
    )

    // 20 Dec 12:34 / 20 Dec 2021 12:23
    class ShortDateTime(closeDateRange: Int? = null) : Format(
        closeDateRange = closeDateRange,
        time = HourMinuteTimePattern,
        currentYear = DayMonthShortPattern,
        otherYear = DayMonthYearShortPattern
    )

    // 20 December 12:34 / 20 December 2021 12:34
    class LongDateTime(closeDateRange: Int? = null) : Format(
        closeDateRange = closeDateRange,
        time = HourMinuteTimePattern,
        currentYear = DayMonthLongPattern,
        otherYear = DayMonthYearLongPattern
    )
}

sealed class TimePattern private constructor(val patternString: String) {
    internal object HourMinuteTimePattern : TimePattern("HH:mm")
    internal object HourMinuteSecondTimePattern : TimePattern("HH:mm:ss")
}

sealed class DatePattern private constructor(val patternString: String) {
    internal object DayMonthShortPattern : DatePattern("d\u00A0MMM")
    internal object DayMonthLongPattern : DatePattern("d\u00A0MMMM")
    internal object DayMonthYearShortPattern : DatePattern("d\u00A0MMM\u00A0yyyy")
    internal object DayMonthYearLongPattern : DatePattern("d\u00A0MMMM\u00A0yyyy")
}
