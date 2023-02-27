package com.hejwesele.settings.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LogoutViewModel @Inject constructor(
    // private val navigator: Navigator
) : ViewModel() {

    fun logout() = viewModelScope.launch {
        // navigator.navigate(Destinations.authentication)
    }
}
