package com.hejwesele.internet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class InternetConnectionViewModel @Inject constructor(
    private val internetConnectionManager: InternetConnectionManager
) : ViewModel() {

    lateinit var connectionState: StateFlow<InternetConnectionState>

    init {
        viewModelScope.launch {
            connectionState = internetConnectionManager
                .observeConnectionState()
                .stateIn(
                    scope = this,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = internetConnectionManager.currentConnectionState
                )
        }
    }
}
