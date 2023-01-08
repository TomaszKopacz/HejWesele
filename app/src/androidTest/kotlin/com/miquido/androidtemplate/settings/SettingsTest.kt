package com.miquido.androidtemplate.settings

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.common.truth.Truth.assertThat
import com.miquido.android.customtabs.LocalCustomTabs
import com.miquido.android.navigation.Destinations
import com.miquido.android.navigation.TestNavigation
import com.miquido.androidtemplate.BuildConfig
import com.miquido.androidtemplate.TestActivity
import com.miquido.androidtemplate.UiTest
import com.miquido.androidtemplate.util.TestAnimatedNavHost
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterialNavigationApi::class)
@HiltAndroidTest
@UiTest
class SettingsTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            CompositionLocalProvider(LocalCustomTabs provides composeRule.activity.customTabs) {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    TestAnimatedNavHost(bottomSheetNavigator) { startRoute, navController ->
                        settingsGraph(startRoute, navController)
                    }
                }
            }
        }
    }

    @Test
    fun testSettings(): Unit = with(composeRule) {
        onNodeWithText("Settings").assertIsDisplayed()
        onNodeWithText("Theme").assertIsDisplayed()
        onNode(hasText("System default") or hasText("Light")).assertIsDisplayed()
        onNodeWithText("Open Source").assertIsDisplayed()
        onNodeWithText("Logout").assertIsDisplayed()
        onNodeWithText("Android Template Debug", substring = true)
            .assertIsDisplayed()
            .assertTextEquals("Android Template Debug ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }

    @Test
    fun testTheme(): Unit = with(composeRule) {
        onNodeWithText("Theme").assertHasClickAction().performClick()
        onNode(hasText("Theme") and hasNoClickAction()).assertIsDisplayed()
        // TODO extend test with matching bottom sheet elements
    }

    @Test
    fun testLicenses(): Unit = with(composeRule) {
        onNodeWithText("Open Source").assertHasClickAction().performClick()
        onNodeWithText("Open Source").assertIsDisplayed()
        onNodeWithText("The application is built with the following open source projects.").assertIsDisplayed()
        // TODO find out a concise and scalable way to validate all LazyColumn items
    }

    @Test
    fun testLogout(): Unit = with(composeRule) {
        onNodeWithText("Logout").assertHasClickAction().performClick()
        assertThat(TestNavigation.navigatedTo.last()).isEqualTo(Destinations.authentication)
    }
}
