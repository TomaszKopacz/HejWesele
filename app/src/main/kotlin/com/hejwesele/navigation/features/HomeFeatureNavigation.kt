package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.home.IHomeNavigation
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.LoginNavGraph
import com.hejwesele.navigation.MainNavGraph
import com.hejwesele.navigation.SettingsNavGraph

class HomeFeatureNavigation(
    private val navController: NavController
) : IHomeNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openInformation() {
        navController.navigate(SettingsNavGraph.route)
    }

    override fun openLogin() {
        navController.navigate(LoginNavGraph.route) {
            popUpTo(MainNavGraph.route) { inclusive = true }
        }
    }
}
