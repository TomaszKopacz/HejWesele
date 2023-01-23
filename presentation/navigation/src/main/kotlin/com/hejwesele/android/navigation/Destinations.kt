package com.hejwesele.android.navigation

object Destinations {
    val authentication = Destination(
        route = AuthenticationDirection.route,
        options = { popUpTo(MainDirection.route) { inclusive = true } }
    )
    fun main(userId: Int) = Destination(
        route = MainDirection.toDestination(userId),
        options = { popUpTo(AuthenticationDirection.route) { inclusive = true } }
    )
}
