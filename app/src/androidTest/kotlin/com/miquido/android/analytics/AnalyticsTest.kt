package com.miquido.android.analytics

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Android test was needed, because context is used to initialize FirebaseAnalytics instance
 * The test will fail when run during service interruption.
 */
@HiltAndroidTest
class AnalyticsTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var analytics: Analytics

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testSampleAnalyticsEventLog_success() {
        analytics.logEvent(name = "eventName")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSampleAnalyticsEventLog_tooLongEventName() {
        val eventName = "veryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongEventName"
        analytics.logEvent(eventName)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSampleAnalyticsEventLog_wrongParam() {
        analytics.logEvent(
            name = "eventName",
            params = mapOf("paramKey" to setOf(1))
        )
    }
}
