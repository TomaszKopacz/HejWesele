package com.miquido.androidtemplate.authentication

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.miquido.android.navigation.Destinations
import com.miquido.android.navigation.TestNavigation
import com.miquido.androidtemplate.TestActivity
import com.miquido.androidtemplate.UiTest
import com.miquido.androidtemplate.util.TestAnimatedNavHost
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UiTest
class AuthenticationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            TestAnimatedNavHost { startRoute, navController ->
                authenticationGraph(startRoute, navController)
            }
        }
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun testAuthentication(): Unit = with(composeRule) {
        runTest {
            onNodeWithText("Login").assertIsDisplayed().assertHasClickAction().performClick()
            advanceTimeBy(5000)
            runCurrent()
            assertThat(TestNavigation.navigatedTo.last()).isEqualTo(Destinations.main(userId = 44))
        }
    }
}
