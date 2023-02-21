package com.hejwesele.android

import com.hejwesele.android.config.switcher.destinations.ConfigurationScreenDestination
import com.hejwesele.authentication.destinations.AuthenticationDestination
import com.hejwesele.dashboard.destinations.DashboardDestination
import com.hejwesele.onboarding.destinations.OnboardingDestination
import com.hejwesele.settings.SettingsNavGraph
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

object RootNavGraph : NavGraphSpec {
    override val route: String = "root"
    override val startRoute: Route = AuthenticationDestination
    override val destinationsByRoute: Map<String, DestinationSpec<*>> = listOf(
        OnboardingDestination,
        AuthenticationDestination,
        ConfigurationScreenDestination,
        DashboardDestination
    ).associateBy { it.route }
    override val nestedNavGraphs: List<NavGraphSpec> = listOf(
        SettingsNavGraph
    )
}