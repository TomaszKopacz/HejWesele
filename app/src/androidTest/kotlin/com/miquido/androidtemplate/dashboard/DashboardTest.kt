package com.miquido.androidtemplate.dashboard

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.miquido.androidtemplate.R
import com.miquido.androidtemplate.TestActivity
import com.miquido.androidtemplate.UiTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UiTest
class DashboardTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            Dashboard(appName = stringResource(id = R.string.app_name), userId = 44)
        }
    }

    @Test
    fun testDashboard(): Unit = with(composeRule) {
        onNodeWithText("Android Template Debug").assertIsDisplayed()
        onNodeWithText("Hello Jetpack Compose! User id: 44").assertIsDisplayed()
    }
}
