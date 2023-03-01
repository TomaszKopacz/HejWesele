package com.hejwesele.android.mvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface StateHandler<UiState> {
    val states: StateFlow<UiState>
    fun setState(state: UiState)
    fun updateState(block: UiState.() -> UiState)
}

private class StateHandlerImpl<UiState>(defaultState: UiState) : StateHandler<UiState> {
    private val _state = MutableStateFlow(defaultState)
    override val states: StateFlow<UiState> = _state.asStateFlow()

    override fun setState(state: UiState) {
        _state.value = state
    }

    override fun updateState(block: UiState.() -> UiState) {
        _state.value = block(_state.value)
    }
}

open class StateViewModel<UiState>(defaultState: UiState) :
    ViewModel(),
    StateHandler<UiState> by StateHandlerImpl(defaultState)
