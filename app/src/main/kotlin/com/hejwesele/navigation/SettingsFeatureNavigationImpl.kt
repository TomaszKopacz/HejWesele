package com.hejwesele.navigation

import androidx.navigation.NavController
import com.hejwesele.settings.destinations.TermsAndConditionsDestination
import com.hejwesele.settings.navigation.SettingsFeatureNavigation
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class SettingsFeatureNavigationImpl(
    private val navController: NavController
) : SettingsFeatureNavigation, CommonNavigation by CommonNavigatorImpl(navController) {

    override fun openTermsAndConditions() {
        navController.navigate(TermsAndConditionsDestination within SettingsNavGraph)
    }
}
