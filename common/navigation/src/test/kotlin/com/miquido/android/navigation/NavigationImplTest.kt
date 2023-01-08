package com.miquido.android.navigation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NavigationImplTest {

    private val navigation = NavigationImpl()

    @Test
    fun `emit events correctly`() = runBlocking {
        navigation.events.test {
            navigation.navigate(Destination("main"))
            navigation.navigate(Destination("auth"))

            assertThat(awaitItem()).isEqualTo(Destination("main"))
            assertThat(awaitItem()).isEqualTo(Destination("auth"))
            cancelAndConsumeRemainingEvents()
        }
    }
}
