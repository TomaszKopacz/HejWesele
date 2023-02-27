package com.hejwesele.authentication

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationViewModelTest {

    private val navigator: Navigator = mock()

    private val viewModel = AuthenticationViewModel(navigator)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `show loading, loggingIn message and navigate to Main when authenticate action is performed`() = runTest {
        viewModel.actions.test {
            viewModel.authenticate()
            advanceTimeBy(5000)
            runCurrent()

            assertThat(viewModel.states.value).isEqualTo(AuthenticationUiState(isAuthenticating = true))
            assertThat(awaitItem()).isEqualTo(AuthenticationUiAction.ShowLoggingInMessage)
            verify(navigator).navigate(Destinations.main(userId = 44))
        }
    }
}
