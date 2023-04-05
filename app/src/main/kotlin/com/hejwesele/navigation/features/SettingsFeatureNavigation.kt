package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.SettingsNavGraph
import com.hejwesele.settings.ISettingsFeatureNavigation
import com.hejwesele.settings.destinations.PrivacyPolicyDestination
import com.hejwesele.settings.destinations.TermsAndConditionsDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class SettingsFeatureNavigation(
    private val navController: NavController
) : ISettingsFeatureNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openTermsAndConditions() {
        navController.navigate(TermsAndConditionsDestination within SettingsNavGraph)
    }

    override fun openPrivacyPolicy() {
        navController.navigate(PrivacyPolicyDestination within SettingsNavGraph)
    }
}
