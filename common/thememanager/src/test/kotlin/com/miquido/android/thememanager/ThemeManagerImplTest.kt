package com.miquido.android.thememanager

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.miquido.android.osinfo.OsInfo
import com.miquido.android.preferences.Entry
import com.miquido.android.preferences.Preferences
import com.miquido.android.thememanager.Theme.DARK
import com.miquido.android.thememanager.Theme.LIGHT
import com.miquido.android.thememanager.Theme.SYSTEM_DEFAULT
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeManagerImplTest {

    private val osInfo: OsInfo = mock()
    private val preferences: Preferences = mock()
    private val appCompatDelegateWrapper: AppCompatDelegateWrapper = mock()
    private val dispatcher = StandardTestDispatcher()

    private val themeManager = ThemeManagerImpl(
        osInfo = osInfo,
        preferences = preferences,
        appCompatDelegateWrapper = appCompatDelegateWrapper,
        ioDispatcher = dispatcher
    )

    @Test
    fun `test get available themes on Android 10 or higher`() {
        whenever(osInfo.isQOrHigher).thenReturn(true)
        assertThat(themeManager.getAvailableThemes())
            .containsExactly(SYSTEM_DEFAULT, LIGHT, DARK)
            .inOrder()
    }

    @Test
    fun `test get available themes on Android before 10`() {
        whenever(osInfo.isQOrHigher).thenReturn(false)
        assertThat(themeManager.getAvailableThemes())
            .containsExactly(LIGHT, DARK)
            .inOrder()
    }

    @Test
    fun `test observe selected theme on Android 10 or higher`() = runTest(dispatcher) {
        val flow = flowOf(null, "system_default", "light", "dark")

        whenever(preferences.observe<String>(any())).thenReturn(flow)
        whenever(osInfo.isQOrHigher).thenReturn(true)

        themeManager.observeSelectedTheme().test {
            assertThat(awaitItem()).isEqualTo(SYSTEM_DEFAULT)
            assertThat(awaitItem()).isEqualTo(SYSTEM_DEFAULT)
            assertThat(awaitItem()).isEqualTo(LIGHT)
            assertThat(awaitItem()).isEqualTo(DARK)
            awaitComplete()
        }
    }

    @Test
    fun `test observe selected theme on Android before 10`() = runTest(dispatcher) {
        val flow = flowOf(null, "light", "dark")

        whenever(preferences.observe<String>(any())).thenReturn(flow)
        whenever(osInfo.isQOrHigher).thenReturn(false)

        themeManager.observeSelectedTheme().test {
            assertThat(awaitItem()).isEqualTo(LIGHT)
            assertThat(awaitItem()).isEqualTo(LIGHT)
            assertThat(awaitItem()).isEqualTo(DARK)
            awaitComplete()
        }
    }

    @Test
    fun `test get selected theme`() = runTest(dispatcher) {
        whenever(preferences.get<String>(any())).thenReturn("dark")
        assertThat(themeManager.getSelectedTheme()).isEqualTo(DARK)
    }

    @Test
    fun `test switch theme to system default`() = runTest(dispatcher) {
        themeManager.switchTheme(SYSTEM_DEFAULT)
        verify(appCompatDelegateWrapper).setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        with(argumentCaptor<Entry<String>>()) {
            verify(preferences).put(capture())
            assertThat(allValues).hasSize(1)
            assertThat(firstValue.value).isEqualTo("system_default")
        }
    }

    @Test
    fun `test switch theme to light`() = runTest(dispatcher) {
        themeManager.switchTheme(LIGHT)
        verify(appCompatDelegateWrapper).setDefaultNightMode(MODE_NIGHT_NO)
        with(argumentCaptor<Entry<String>>()) {
            verify(preferences).put(capture())
            assertThat(allValues).hasSize(1)
            assertThat(firstValue.value).isEqualTo("light")
        }
    }

    @Test
    fun `test switch theme to dark`() = runTest(dispatcher) {
        themeManager.switchTheme(DARK)
        verify(appCompatDelegateWrapper).setDefaultNightMode(MODE_NIGHT_YES)
        with(argumentCaptor<Entry<String>>()) {
            verify(preferences).put(capture())
            assertThat(allValues).hasSize(1)
            assertThat(firstValue.value).isEqualTo("dark")
        }
    }

    @Test
    fun `test apply theme`() = runTest(dispatcher) {
        whenever(preferences.get<String>(any())).thenReturn("dark")
        themeManager.applyTheme()
        verify(appCompatDelegateWrapper).setDefaultNightMode(MODE_NIGHT_YES)
    }

    @Test
    fun `test apply theme when no theme stored on Android 10 or higher`() = runTest(dispatcher) {
        whenever(preferences.get<String>(any())).thenReturn(null)
        whenever(osInfo.isQOrHigher).thenReturn(true)
        themeManager.applyTheme()
        verify(appCompatDelegateWrapper).setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
    }

    @Test
    fun `test apply theme when no theme stored on Android before 10`() = runTest(dispatcher) {
        whenever(preferences.get<String>(any())).thenReturn(null)
        whenever(osInfo.isQOrHigher).thenReturn(false)
        themeManager.applyTheme()
        verify(appCompatDelegateWrapper).setDefaultNightMode(MODE_NIGHT_NO)
    }
}
