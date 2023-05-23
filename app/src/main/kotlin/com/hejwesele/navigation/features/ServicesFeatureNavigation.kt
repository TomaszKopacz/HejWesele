package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.LoginNavGraph
import com.hejwesele.navigation.MainNavGraph
import com.hejwesele.services.IServicesNavigation
import com.hejwesele.services.details.destinations.ServiceDetailsDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class ServicesFeatureNavigation(
    private val navController: NavController
) : IServicesNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openServiceDetails(serviceId: String) {
        navController.navigate(
            ServiceDetailsDestination(serviceId = serviceId) within MainNavGraph
        )
    }

    override fun logout() {
        navController.navigate(LoginNavGraph.route) {
            popUpTo(MainNavGraph.route) { inclusive = true }
        }
    }
}
