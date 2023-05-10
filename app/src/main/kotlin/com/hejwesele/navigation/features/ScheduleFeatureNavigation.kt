package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.LoginNavGraph
import com.hejwesele.navigation.MainNavGraph
import com.hejwesele.schedule.IScheduleNavigation

class ScheduleFeatureNavigation(
    private val navController: NavController
) : IScheduleNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun logout() {
        navController.navigate(LoginNavGraph.route) {
            popUpTo(MainNavGraph.route) { inclusive = true }
        }
    }
}
