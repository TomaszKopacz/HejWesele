package com.hejwesele.authentication

import com.hejwesele.android.navigation.ModuleDestinations
import com.hejwesele.android.navigation.invoke
import com.hejwesele.android.navigation.Navigator
import com.hejwesele.authentication.destinations.AuthenticationDestination
import com.ramcosta.composedestinations.navigation.popUpTo
import javax.inject.Inject

internal interface AuthenticationNavigator {
    suspend fun openConfiguration()
    suspend fun openDashboard()
}

internal class AuthenticationNavigatorImpl @Inject constructor(
    private val navigator: Navigator,
    private val modules: ModuleDestinations
) : AuthenticationNavigator {

    override suspend fun openConfiguration() {
        navigator.navigate(modules.configuration()())
    }

    override suspend fun openDashboard() {
        navigator.navigate(modules.home()()) {
            popUpTo(AuthenticationDestination) { inclusive = true }
        }
    }

}