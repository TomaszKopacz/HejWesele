package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.LoginNavGraph
import com.hejwesele.navigation.MainNavGraph
import com.hejwesele.services.IServicesNavigation
import com.ramcosta.composedestinations.dynamic.within
import com.hejwesele.services.details.destinations.ServiceDetailsDestination
import com.ramcosta.composedestinations.navigation.navigate

class ServicesFeatureNavigation(
    private val navController: NavController
) : IServicesNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openServiceDetails(
        title: String,
        name: String?,
        description: String
    ) {
        navController.navigate(
            ServiceDetailsDestination(
                title = title,
                name = name,
                description = description
            ) within MainNavGraph
        )
    }

    override fun logout() {
        navController.navigate(LoginNavGraph.route) {
            popUpTo(MainNavGraph.route) { inclusive = true }
        }
    }
}
