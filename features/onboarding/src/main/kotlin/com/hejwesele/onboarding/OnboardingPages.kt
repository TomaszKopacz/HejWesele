package com.hejwesele.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hejwesele.onboarding.R

enum class OnboardingPages(
    @StringRes val title: Int,
    @DrawableRes val image: Int,
    @StringRes val description: Int
) {
    FIRST_PAGE(
        title = R.string.first_onboarding_page_title,
        image = R.drawable.first_onboarding_page,
        description = R.string.first_onboarding_page_description
    ),
    SECOND_PAGE(
        title = R.string.second_onboarding_page_title,
        image = R.drawable.second_onboarding_page,
        description = R.string.second_onboarding_page_description
    ),
    LAST_PAGE(
        title = R.string.last_onboarding_page_title,
        image = R.drawable.last_onboarding_page,
        description = R.string.last_onboarding_page_description
    )
}
