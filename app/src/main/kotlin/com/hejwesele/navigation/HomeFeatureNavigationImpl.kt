package com.hejwesele.navigation

import androidx.navigation.NavController
import com.hejwesele.home.navigation.HomeFeatureNavigation

class HomeFeatureNavigationImpl(
    private val navController: NavController
) : HomeFeatureNavigation, CommonNavigation by CommonNavigatorImpl(navController) {

    override fun openNothing() {
        /*navController.navigate(SomeDestination)*/
    }

    // override fun cards(): @Composable () -> Unit = { Text(text = "Cards") }

    // override fun carwash(): @Composable () -> Unit = { Text(text = "CarWash") }

    // override fun highway(): @Composable () -> Unit = { Text(text = "Highway") }

}
