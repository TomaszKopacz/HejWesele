package com.hejwesele.settings.theme

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.hejwesele.android.thememanager.Theme.DARK
import com.hejwesele.android.thememanager.Theme.LIGHT
import com.hejwesele.android.thememanager.Theme.SYSTEM_DEFAULT
import com.hejwesele.android.thememanager.ThemeManager
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ThemeBottomSheetViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val themeManager: ThemeManager = mock {
        on { getAvailableThemes() }.thenReturn(listOf(SYSTEM_DEFAULT, LIGHT, DARK))
    }
    private val viewModel = ThemeBottomSheetViewModel(themeManager)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test UI state`() {
        val state = ThemeBottomSheetUiState(themes = listOf(SYSTEM_DEFAULT, LIGHT, DARK))
        assertThat(viewModel.states.value).isEqualTo(state)
    }

    @Test
    fun `test observe selected theme`() = runTest(dispatcher) {
        val flow = flowOf(SYSTEM_DEFAULT, LIGHT, DARK)
        whenever(themeManager.observeSelectedTheme()).thenReturn(flow)
        viewModel.observeSelectedTheme().test {
            assertThat(awaitItem()).isEqualTo(SYSTEM_DEFAULT)
            assertThat(awaitItem()).isEqualTo(LIGHT)
            assertThat(awaitItem()).isEqualTo(DARK)
            awaitComplete()
        }
    }

    @Test
    fun `test switch theme`() = runTest(dispatcher) {
        viewModel.switchTheme(DARK)
        runCurrent()
        verify(themeManager).switchTheme(DARK)
    }
}
