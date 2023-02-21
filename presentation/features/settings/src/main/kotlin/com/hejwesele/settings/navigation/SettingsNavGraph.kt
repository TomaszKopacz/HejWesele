package com.hejwesele.settings.navigation

import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph(route = "settings")
internal annotation class SettingsNavGraph(
    val start: Boolean = false
)
