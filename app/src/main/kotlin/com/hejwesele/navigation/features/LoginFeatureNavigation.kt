package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.ILoginNavigation
import com.hejwesele.destinations.QrScannerDestination
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.LoginNavGraph
import com.hejwesele.navigation.MainNavGraph
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo

class LoginFeatureNavigation(
    private val navController: NavController
) : ILoginNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openEvent() {
        navController.navigate(MainNavGraph) {
            popUpTo(LoginNavGraph) { inclusive = true }
        }
    }

    override fun openQrScanner() {
        navController.navigate(QrScannerDestination within LoginNavGraph)
    }
}
