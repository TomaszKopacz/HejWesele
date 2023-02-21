package com.hejwesele.android.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

typealias ModuleDestination = TypedModuleDestination<*>

typealias TypedModuleDestination<T> = ModuleDestinationSpec<T>

typealias DirectionModuleDestination = TypedModuleDestination<Unit>

sealed class ModuleDestinationSpec<T> {

    abstract val route: Route
    abstract operator fun invoke(arg: T): ComposeDirection

    class DirectDestinationSpec<T>(
        val destinationSpec: DestinationSpec<T>
    ) : TypedModuleDestination<T>() {
        override val route: Route = destinationSpec

        override fun invoke(arg: T): ComposeDirection = destinationSpec(arg)
    }

    class NestedDestinationSpec(
        val graphSpec: NavGraphSpec
    ) : DirectionModuleDestination() {
        override val route: Route = graphSpec

        override fun invoke(arg: Unit): ComposeDirection = graphSpec
    }
}

operator fun DirectionModuleDestination.invoke() = invoke(Unit)