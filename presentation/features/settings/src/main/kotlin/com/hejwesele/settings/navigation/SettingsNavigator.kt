package com.hejwesele.settings.navigation

import com.hejwesele.android.navigation.ModuleDestinations
import com.hejwesele.android.navigation.Navigator
import com.hejwesele.android.navigation.invoke
import com.hejwesele.settings.destinations.LicensesDestination
import com.hejwesele.settings.destinations.ThemeBottomSheetDestination
import com.ramcosta.composedestinations.navigation.popUpTo
import javax.inject.Inject

internal interface SettingsNavigator {
    suspend fun openThemeSwitcher()
    suspend fun openLicenses()
    suspend fun openLogin()
}

internal class SettingsNavigatorImpl @Inject constructor(
    private val navigator: Navigator,
    private val modules: ModuleDestinations
) : SettingsNavigator {
    override suspend fun openThemeSwitcher() {
        navigator.navigate(ThemeBottomSheetDestination)
    }

    override suspend fun openLicenses() {
        navigator.navigate(LicensesDestination)
    }

    override suspend fun openLogin() {
        navigator.navigate(modules.auth()()) {
            popUpTo(modules.dashboard().route) {
                inclusive = true
            }
        }
    }

}