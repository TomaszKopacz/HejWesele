package com.hejwesele.navigation

import com.hejwesele.event.destinations.MainDestination
import com.hejwesele.gallery.preview.destinations.PreviewDestination
import com.hejwesele.settings.destinations.SettingsOverviewDestination
import com.hejwesele.settings.destinations.TermsAndConditionsDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

// TODO - maybe delete, replace with direct Main route instead of nested graph
object MainNavGraph : NavGraphSpec {
    override val route: String = "mainNavGraph"
    override val startRoute: Route = MainDestination routedIn this
    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        MainDestination,
        PreviewDestination
    ).routedIn(this).associateBy { it.route }
}

object SettingsNavGraph : NavGraphSpec {
    override val route: String = "settingsNavGraph"
    override val startRoute: Route = SettingsOverviewDestination routedIn this
    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        SettingsOverviewDestination,
        TermsAndConditionsDestination
    ).routedIn(this).associateBy { it.route }
}