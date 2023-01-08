package com.miquido.androidtemplate.main

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class MainTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            Main(appName = stringResource(id = R.string.app_name), userId = 44)
        }
    }

    @Test
    fun testMain(): Unit = with(composeRule) {
        onNodeWithText("Android Template Debug").assertIsDisplayed()
        onNodeWithText("Dashboard").assertIsDisplayed()
        onNodeWithText("Settings").assertIsDisplayed()

        onNodeWithText("Settings").performClick()
        onNodeWithText("Logout").assertIsDisplayed()

        onNodeWithText("Dashboard").performClick()
        onNodeWithText("Android Template Debug").assertIsDisplayed()
    }
}
