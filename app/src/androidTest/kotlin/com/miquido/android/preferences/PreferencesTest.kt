package com.miquido.android.preferences

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PreferencesTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var preferences: Preferences

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testPutSingleKey() {
        val key = key<String>("key")
        runBlocking {
            preferences.put(key to "value")
            assertThat(preferences.keys()).containsExactly("key")
            assertThat(preferences.contains(key)).isTrue()
            assertThat(preferences.get(key)).isEqualTo("value")
        }
    }

    @Test
    fun testPutMultipleKeys() {
        val key = key<String>("key")
        val key2 = key<String>("key2")
        runBlocking {
            preferences.putAll(setOf(key to "value", key2 to "value2"))
            assertThat(preferences.keys()).containsExactly("key", "key2")
        }
    }

    @Test
    fun testUpdate() {
        val key = key<String>("key")
        runBlocking {
            preferences.put(key to "value")
            preferences.update { map -> map.apply { put(key, "value1.1") } }
            assertThat(preferences.get(key)).isEqualTo("value1.1")
        }
    }

    @Test
    fun testObserve() {
        val key = key<String>("key")
        runBlocking {
            preferences.observe(key).test {
                assertThat(awaitItem()).isEqualTo(null)
                preferences.put(key to "value")
                assertThat(awaitItem()).isEqualTo("value")
                preferences.put(key to "value1.1")
                assertThat(awaitItem()).isEqualTo("value1.1")
                preferences.put(key to null)
                assertThat(awaitItem()).isEqualTo(null)
                cancel()
            }
        }
    }

    @Test
    fun testClear() {
        val key = key<String>("key")
        runBlocking {
            preferences.put(key to "value")
            preferences.clear()
            assertThat(preferences.keys()).isEmpty()
        }
    }
}
