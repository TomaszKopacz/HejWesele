package com.miquido.androidtemplate.onboarding

sealed class OnboardingAnalyticsEvent(open val name: String) {
    object Displayed : OnboardingAnalyticsEvent("onboarding_displayed")
}
