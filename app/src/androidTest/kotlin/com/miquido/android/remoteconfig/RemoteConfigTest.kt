package com.miquido.android.remoteconfig

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Due to no possibility to replace fetcher in Firebase Remote Config API,
 * this test uses real Firebase Remote Config service.
 * The test will fail when run during service interruption.
 */
@HiltAndroidTest
class RemoteConfigTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Before
    fun setUp() {
        hiltRule.inject()
        remoteConfig.init()
    }

    @Test
    fun testRemoteConfigInitializationAndFetch() {
        val attempts = 10
        val key = "test"
        var value: String? = null
        for (i in 0 until attempts) {
            value = remoteConfig.getStringOrNull(key)
            if (value != null) break else Thread.sleep(1000)
        }
        assertThat(value).isEqualTo("value")
    }
}
