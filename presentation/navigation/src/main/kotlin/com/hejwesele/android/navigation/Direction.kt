package com.hejwesele.android.navigation

import androidx.navigation.NamedNavArgument

interface Direction {
    val route: String
    val args: List<NamedNavArgument>
}
