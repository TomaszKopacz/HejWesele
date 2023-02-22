package com.hejwesele.android.root

import com.hejwesele.android.navigation.DirectionModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.android.navigation.ModuleDestinationSpec.NestedDestinationSpec
import com.hejwesele.android.navigation.ROOT_ROUTE
import com.hejwesele.android.navigation.StartModuleDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import javax.inject.Inject

internal class RootNavGraph @Inject constructor(
    @StartModuleDestination start: DirectionModuleDestination,
    destinations: Map<String, @JvmSuppressWildcards ModuleDestinationSpec<*>>
) : NavGraphSpec {
    override val route: String = ROOT_ROUTE
    override val startRoute: Route = start.route
    override val destinationsByRoute: Map<String, DestinationSpec<*>> = destinations.values
        .filterIsInstance<DirectDestinationSpec<*>>()
        .map { it.destinationSpec }
        .associateBy { it.route }
    override val nestedNavGraphs: List<NavGraphSpec> = destinations.values
        .filterIsInstance<NestedDestinationSpec>()
        .map { it.graphSpec }
}
