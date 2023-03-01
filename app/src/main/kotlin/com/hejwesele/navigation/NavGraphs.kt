package com.hejwesele.navigation

import com.hejwesele.gallery.preview.destinations.GalleryPreviewDestination
import com.hejwesele.main.destinations.MainDestination
import com.hejwesele.settings.destinations.SettingsOverviewDestination
import com.hejwesele.settings.destinations.SettingsTermsAndConditionsDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

internal fun getRootGraph(startRoute: Route) = object : NavGraphSpec {
    override val route = "rootNavGraph"
    override val startRoute = startRoute
    override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
    override val nestedNavGraphs = listOf(
        MainNavGraph,
        SettingsNavGraph
    )
}

internal object MainNavGraph : NavGraphSpec {
    override val route: String = "mainNavGraph"
    override val startRoute: Route = MainDestination routedIn this
    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        MainDestination,
        GalleryPreviewDestination
    ).routedIn(this).associateBy { it.route }
}

internal object SettingsNavGraph : NavGraphSpec {
    override val route: String = "settingsNavGraph"
    override val startRoute: Route = SettingsOverviewDestination routedIn this
    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        SettingsOverviewDestination,
        SettingsTermsAndConditionsDestination
    ).routedIn(this).associateBy { it.route }
}
