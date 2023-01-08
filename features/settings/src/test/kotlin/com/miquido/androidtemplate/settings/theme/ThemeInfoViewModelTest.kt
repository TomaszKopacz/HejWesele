package com.miquido.androidtemplate.settings.theme

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.miquido.android.thememanager.Theme.DARK
import com.miquido.android.thememanager.Theme.LIGHT
import com.miquido.android.thememanager.Theme.SYSTEM_DEFAULT
import com.miquido.android.thememanager.ThemeManager
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeInfoViewModelTest {

    private val themeManager: ThemeManager = mock()
    private val viewModel = ThemeInfoViewModel(themeManager)

    @Test
    fun `test observe selected theme`() = runTest {
        val flow = flowOf(SYSTEM_DEFAULT, LIGHT, DARK)
        whenever(themeManager.observeSelectedTheme()).thenReturn(flow)
        viewModel.observeSelectedTheme().test {
            assertThat(awaitItem()).isEqualTo(SYSTEM_DEFAULT)
            assertThat(awaitItem()).isEqualTo(LIGHT)
            assertThat(awaitItem()).isEqualTo(DARK)
            awaitComplete()
        }
    }
}
