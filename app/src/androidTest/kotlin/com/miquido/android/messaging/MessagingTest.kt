package com.miquido.android.messaging

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * This test uses real Firebase Cloud Messaging service.
 * The test will fail when run during service interruption.
 */
@HiltAndroidTest
class MessagingTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var messaging: Messaging

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testGetToken() {
        val token: String
        runBlocking { token = messaging.getToken() }
        assertThat(token).isNotEmpty()
    }
}
