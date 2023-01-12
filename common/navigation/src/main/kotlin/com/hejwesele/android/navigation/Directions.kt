package com.hejwesele.android.navigation

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object AuthenticationDirection : Direction {
    override val route = "authentication"
    override val args = emptyList<NamedNavArgument>()
}

object MainDirection : Direction {
    private const val userIdKey = "userId"

    override val route = "main/{$userIdKey}"
    override val args = listOf(navArgument(userIdKey) { type = NavType.IntType })

    internal fun toDestination(userId: Int) = route.replace("{$userIdKey}", userId.toString())

    fun Bundle.getUserId(): Int = getInt(userIdKey)
}

object OnboardingDirection : Direction {
    override val route = "onboarding"
    override val args = emptyList<NamedNavArgument>()
}

fun getStartDestination(onboardingDisplayed: Boolean) =
    if (onboardingDisplayed) AuthenticationDirection.route else OnboardingDirection.route
