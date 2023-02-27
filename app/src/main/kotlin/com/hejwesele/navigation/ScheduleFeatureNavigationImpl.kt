package com.hejwesele.navigation

import androidx.navigation.NavController
import com.hejwesele.schedule.navigation.ScheduleFeatureNavigation

class ScheduleFeatureNavigationImpl(
    private val navController: NavController
) : ScheduleFeatureNavigation, CommonNavigation by CommonNavigatorImpl(navController) {

    override fun openNothing() {
        /*navController.navigate(SomeDestination)*/
    }
}
