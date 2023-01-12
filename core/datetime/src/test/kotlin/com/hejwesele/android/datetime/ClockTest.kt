package com.hejwesele.android.datetime

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test

class ClockTest {

    private val clock = mock<Clock> {
        on { now() }.thenReturn("2021-12-18T23:00:00Z".toInstant())
        on { zone() }.thenReturn(TimeZone.of("UTC+3"))
    }

    @Test
    fun `test getting sample LocalDateTime`() {
        assertThat(clock.localDateTime())
            .isEqualTo(LocalDateTime(year = 2021, monthNumber = 12, dayOfMonth = 19, hour = 2, minute = 0, second = 0))
    }

    @Test
    fun `test getting sample LocalDate`() {
        assertThat(clock.localDate())
            .isEqualTo(LocalDate(year = 2021, monthNumber = 12, dayOfMonth = 19))
    }
}
