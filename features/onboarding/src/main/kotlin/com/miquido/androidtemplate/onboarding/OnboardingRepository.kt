package com.miquido.androidtemplate.onboarding

import com.miquido.android.preferences.Preferences
import com.miquido.android.preferences.key
import javax.inject.Inject

interface OnboardingRepository {
    suspend fun getOnboardingDisplayed(): Boolean
    suspend fun setOnboardingDisplayed()
}

class OnboardingRepositoryImpl @Inject constructor(
    private val preferences: Preferences
) : OnboardingRepository {

    override suspend fun getOnboardingDisplayed(): Boolean {
        return preferences.get(KEY_ONBOARDING_DISPLAYED) ?: false
    }

    override suspend fun setOnboardingDisplayed() {
        preferences.put(KEY_ONBOARDING_DISPLAYED to true)
    }

    companion object {
        private val KEY_ONBOARDING_DISPLAYED = key<Boolean>("onboarding_displayed")
    }
}
