package com.hejwesele.navigation

import android.app.Activity
import androidx.navigation.NavController

internal interface ICommonNavigation {
    fun finishApplication()
    fun navigateUp()
}

internal class CommonNavigation(
    private val navController: NavController
) : ICommonNavigation {

    override fun finishApplication() {
        (navController.context as? Activity)?.finishAffinity()
    }

    override fun navigateUp() {
        if (!navController.popBackStack()) {
            (navController.context as? Activity)?.finish()
        }
    }
}
