package com.hejwesele.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.analytics.Analytics
import com.hejwesele.android.coroutines.di.IoDispatcher
import com.hejwesele.android.navigation.Navigator
import com.hejwesele.onboarding.OnboardingAnalyticsEvent.Displayed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository,
    // private val navigator: Navigator,
    private val analytics: Analytics,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun getOnboardingDisplayed() = flow {
        emit(repository.getOnboardingDisplayed())
    }

    fun nextClick() {
        logAnalyticsEvent()
        saveOnboardingDisplayed()
        navigateToAuthenticationScreen()
    }

    private fun saveOnboardingDisplayed() = viewModelScope.launch(ioDispatcher) {
        repository.setOnboardingDisplayed()
    }

    private fun navigateToAuthenticationScreen() = viewModelScope.launch(ioDispatcher) {
        // navigator.navigate(Destinations.authentication)
    }

    private fun logAnalyticsEvent() = viewModelScope.launch(ioDispatcher) {
        analytics.logEvent(Displayed.name)
    }
}
