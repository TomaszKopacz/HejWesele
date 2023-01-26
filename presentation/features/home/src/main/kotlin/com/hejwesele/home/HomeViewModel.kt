package com.hejwesele.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.model.onError
import com.hejwesele.model.onSuccess
import com.hejwesele.usecase.event.GetEvent
import com.hejwesele.usecase.event.ObserveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getEvent: GetEvent,
    private val observeEvent: ObserveEvent
) : StateViewModel<HomeUiState>(HomeUiState.DEFAULT) {

    fun init() {
        viewModelScope.launch {
            getEvent("7AtxeYEUkKYB5M6cUK2g")
                .onSuccess { event ->
                    updateState {
                        copy(eventName = "${event.name} ${event.id}")
                    }
                }
                .onError {
                    Log.d("HWE", "Error: $it")
                }
        }

        viewModelScope.launch {
            observeEvent(
                eventId = "7AtxeYEUkKYB5M6cUK2g",
                coroutineScope = this
            ).collect {
                updateState { copy(eventName = "${it.name} ${it.id}") }
            }
        }
    }
}
