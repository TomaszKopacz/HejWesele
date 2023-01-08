package com.miquido.android.datetimeformatter

import com.google.common.truth.Truth.assertThat
import com.miquido.android.datetime.TestClock
import com.miquido.android.datetimeformatter.Format.Companion.DAY
import com.miquido.android.datetimeformatter.Format.Companion.TODAY
import com.miquido.android.datetimeformatter.Format.Companion.WEEK
import com.miquido.android.datetimeformatter.Format.LongDate
import com.miquido.android.datetimeformatter.Format.LongDateTime
import com.miquido.android.datetimeformatter.Format.ShortDate
import com.miquido.android.datetimeformatter.Format.ShortDateTime
import com.miquido.android.datetimeformatter.Format.Time
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DateTimeFormatterTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dateTimeFormatter: DateTimeFormatter

    @Before
    fun setUp() {
        hiltRule.inject()
        TestClock.now = "2021-12-25T12:00:00+00:00".toInstant()
        TestClock.zone = TimeZone.of("UTC")
    }

    @Test
    fun testTimeFormat() {
        assertThat(dateTimeFormatter.format(
            "2022-12-20T10:36:00+00:00".toInstant(),
            Time
        )).isEqualTo("10:36")
    }

    @Test
    fun testNextDayCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-26T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = DAY)
        )).isEqualTo("tomorrow")
    }

    @Test
    fun testPreviousDayCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-24T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = DAY)
        )).isEqualTo("yesterday")
    }

    @Test
    fun testCurrentDayCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-25T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = DAY)
        )).isEqualTo("today")
    }

    @Test
    fun testDayAfterTomorrowCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-27T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = DAY)
        )).isEqualTo("27\u00A0Dec")
    }

    @Test
    fun testDayBeforeYesterdayCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-23T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = DAY)
        )).isEqualTo("23\u00A0Dec")
    }

    @Test
    fun testCurrentDayFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-25T12:00:00+00:00".toInstant(),
            ShortDate()
        )).isEqualTo("25\u00A0Dec")
    }

    @Test
    fun testTodayCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-25T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = TODAY)
        )).isEqualTo("today")
    }

    @Test
    fun testTodayOutOfScopeCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-24T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = TODAY)
        )).isEqualTo("24\u00A0Dec")
    }

    @Test
    fun testCloseFutureCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2022-01-01T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = WEEK)
        )).isEqualTo("in\u00A07\u00A0days")
    }

    @Test
    fun testClosePastCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-18T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = WEEK)
        )).isEqualTo("7\u00A0days\u00A0ago")
    }

    @Test
    fun testCloseFutureOutOfScopeCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2022-01-02T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = WEEK)
        )).isEqualTo("2\u00A0Jan\u00A02022")
    }

    @Test
    fun testClosePastOutOfScopeCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-17T12:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = WEEK)
        )).isEqualTo("17\u00A0Dec")
    }

    @Test
    fun testLessThanDayDifferenceCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-24T23:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = WEEK)
        )).isEqualTo("yesterday")
    }

    @Test
    fun testMoreThanDayDifferenceCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-26T23:00:00+00:00".toInstant(),
            ShortDate(closeDateRange = WEEK)
        )).isEqualTo("tomorrow")
    }

    @Test
    fun testDifferentTimeZoneCloseDateFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-26T23:00:00-05:00".toInstant(),
            ShortDate(closeDateRange = WEEK)
        )).isEqualTo("in\u00A02\u00A0days")
    }

    @Test
    fun testCurrentAndOtherYearFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-31T10:00:00+00:00".toInstant(),
            LongDate()
        )).isEqualTo("31\u00A0December")
    }

    @Test
    fun testOtherYearFormat() {
        assertThat(dateTimeFormatter.format(
            "2022-01-01T10:00:00+00:00".toInstant(),
            LongDate()
        )).isEqualTo("1\u00A0January\u00A02022")
    }

    @Test
    fun testCurrentDayCloseDateTimeFormat() {
        assertThat(dateTimeFormatter.format(
            "2021-12-25T12:00:00+00:00".toInstant(),
            ShortDateTime(closeDateRange = DAY)
        )).isEqualTo("today\u00A012:00")
    }

    @Test
    fun testDateTimeFormat() {
        assertThat(dateTimeFormatter.format(
            "2020-12-10T12:00:00+00:00".toInstant(),
            LongDateTime()
        )).isEqualTo("10\u00A0December\u00A02020\u00A012:00")
    }
}
