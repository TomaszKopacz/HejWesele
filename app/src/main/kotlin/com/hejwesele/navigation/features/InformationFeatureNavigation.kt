package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.InformationNavGraph
import com.hejwesele.information.IInformationFeatureNavigation
import com.hejwesele.information.destinations.PrivacyPolicyDestination
import com.hejwesele.information.destinations.TermsAndConditionsDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class InformationFeatureNavigation(
    private val navController: NavController
) : IInformationFeatureNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openTermsAndConditions() {
        navController.navigate(TermsAndConditionsDestination within InformationNavGraph)
    }

    override fun openPrivacyPolicy() {
        navController.navigate(PrivacyPolicyDestination within InformationNavGraph)
    }
}
