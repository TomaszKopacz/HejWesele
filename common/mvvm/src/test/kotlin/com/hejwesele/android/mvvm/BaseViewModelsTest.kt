package com.hejwesele.android.mvvm

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelsTest {

    data class UiState(val isLoading: Boolean = true)

    sealed interface UiAction
    object Action : UiAction

    private val stateViewModel = object : StateViewModel<UiState>(UiState()) {}
    private val actionsViewModel = object : ActionsViewModel<UiAction>() {}
    private val stateActionsViewModel = object : StateActionsViewModel<UiState, UiAction>(UiState()) {}

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `test state ViewModel initial state`() {
        assertThat(stateViewModel.states.value).isEqualTo(UiState())
    }

    @Test
    fun `test state ViewModel state set`() {
        stateViewModel.setState(UiState(isLoading = false))
        assertThat(stateViewModel.states.value).isEqualTo(UiState(isLoading = false))
    }

    @Test
    fun `test state ViewModel state update`() {
        stateViewModel.setState(UiState())
        stateViewModel.updateState { copy(isLoading = false) }
        assertThat(stateViewModel.states.value).isEqualTo(UiState(isLoading = false))
    }

    @Test
    fun `test actions ViewModel action emit`() = runTest {
        actionsViewModel.actions.test {
            actionsViewModel.emitAction(Action)
            assertThat(awaitItem()).isEqualTo(Action)
        }
    }

    @Test
    fun `test state actions ViewModel`() = runTest {
        assertThat(stateActionsViewModel.states.value).isEqualTo(UiState())

        stateActionsViewModel.setState(UiState(isLoading = false))
        assertThat(stateActionsViewModel.states.value).isEqualTo(UiState(isLoading = false))

        stateActionsViewModel.updateState { copy(isLoading = true) }
        assertThat(stateActionsViewModel.states.value).isEqualTo(UiState(isLoading = true))

        stateActionsViewModel.actions.test {
            stateActionsViewModel.emitAction(Action)
            assertThat(awaitItem()).isEqualTo(Action)
        }
    }
}
