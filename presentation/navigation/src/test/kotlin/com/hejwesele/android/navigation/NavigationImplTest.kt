package com.hejwesele.android.navigation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NavigationImplTest {

    private val navigation = NavigationImpl()

    @Test
    fun `emit events correctly`() = runBlocking {
        navigation.navActions.test {
            navigation.navigate(Direction("dashboard"))
            navigation.navigate(Direction("settings"))

            assertThat(awaitItem()).isEqualTo(NavAction(Direction("dashboard")))
            assertThat(awaitItem()).isEqualTo(NavAction(Direction("settings")))
            cancelAndConsumeRemainingEvents()
        }
    }
}
