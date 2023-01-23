package com.hejwesele.onboarding

sealed class OnboardingAnalyticsEvent(open val name: String) {
    object Displayed : OnboardingAnalyticsEvent("onboarding_displayed")
}
