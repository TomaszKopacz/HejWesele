package com.hejwesele.onboarding

import com.hejwesele.android.analytics.Analytics
import com.hejwesele.android.navigation.Navigator
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @get:Rule
    val dispatcherRule = MainCoroutinesRule(dispatcher = dispatcher)

    private val repository: OnboardingRepository = mock()
    private val navigator: Navigator = mock()
    private val analytics: Analytics = mock()

    private val viewModel = OnboardingViewModel(repository, navigator, analytics, dispatcher)

    @Test
    fun `navigate to authentication screen when next button is clicked or onboardingStateValue is true`() = runTest {
        whenever(repository.getOnboardingDisplayed()).thenReturn(true)
        viewModel.nextClick()
        runCurrent()
        assertTrue(viewModel.getOnboardingDisplayed().first())
        verify(navigator).navigate(Destinations.authentication)
    }
}
