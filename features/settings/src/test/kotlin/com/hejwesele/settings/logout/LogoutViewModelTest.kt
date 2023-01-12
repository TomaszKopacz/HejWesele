package com.hejwesele.settings.logout

import com.hejwesele.android.navigation.Destinations
import com.hejwesele.android.navigation.Navigator
import com.hejwesele.settings.logout.LogoutViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutViewModelTest {

    private val navigator: Navigator = mock()

    private val viewModel = LogoutViewModel(navigator)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `navigate to authentication on logout`() = runTest {
        viewModel.logout()
        runCurrent()
        verify(navigator).navigate(Destinations.authentication)
    }
}
