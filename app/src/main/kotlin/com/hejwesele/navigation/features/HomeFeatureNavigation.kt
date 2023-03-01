package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.home.IHomeNavigation
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation

class HomeFeatureNavigation(
    private val navController: NavController
) : IHomeNavigation, ICommonNavigation by CommonNavigation(navController) {

    @Suppress("EmptyFunctionBlock")
    override fun openNothing() {}
}
