package com.hejwesele.navigation

import android.app.Activity
import androidx.navigation.NavController

class CommonNavigatorImpl(
    private val navController: NavController
) : CommonNavigation {

    override fun finishApplication() {
        (navController.context as? Activity)?.finishAffinity()
    }

    override fun navigateUp() {
        if (!navController.popBackStack()) {
            (navController.context as? Activity)?.finish()
        }
    }
}

interface CommonNavigation {
    fun finishApplication()
    fun navigateUp()
}
