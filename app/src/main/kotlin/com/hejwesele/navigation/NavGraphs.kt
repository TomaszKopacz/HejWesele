package com.hejwesele.navigation

import com.hejwesele.destinations.LoginDestination
import com.hejwesele.destinations.QrScannerDestination
import com.hejwesele.gallery.destinations.GalleryPreviewDestination
import com.hejwesele.gallery.destinations.PhotoConfirmationDestination
import com.hejwesele.main.destinations.MainDestination
import com.hejwesele.information.destinations.PrivacyPolicyDestination
import com.hejwesele.information.destinations.InformationOverviewDestination
import com.hejwesele.information.destinations.TermsAndConditionsDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

internal fun getRootGraph(startRoute: Route) = object : NavGraphSpec {
    override val route = "rootNavGraph"
    override val startRoute = startRoute
    override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
    override val nestedNavGraphs = listOf(
        LoginNavGraph,
        MainNavGraph,
        InformationNavGraph
    )
}

internal object LoginNavGraph : NavGraphSpec {
    override val route: String = "loginNavGraph"
    override val startRoute: Route = LoginDestination routedIn this
    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        LoginDestination,
        QrScannerDestination
    ).routedIn(this).associateBy { it.route }
}

internal object MainNavGraph : NavGraphSpec {
    override val route: String = "mainNavGraph"
    override val startRoute: Route = MainDestination routedIn this
    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        MainDestination,
        PhotoConfirmationDestination,
        GalleryPreviewDestination
    ).routedIn(this).associateBy { it.route }
}

internal object InformationNavGraph : NavGraphSpec {
    override val route: String = "informationNavGraph"
    override val startRoute: Route = InformationOverviewDestination routedIn this
    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        InformationOverviewDestination,
        TermsAndConditionsDestination,
        PrivacyPolicyDestination
    ).routedIn(this).associateBy { it.route }
}
