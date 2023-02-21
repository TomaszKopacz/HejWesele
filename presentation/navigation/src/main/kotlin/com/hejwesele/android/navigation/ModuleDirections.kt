package com.hejwesele.android.navigation

import javax.inject.Inject

class ModuleDirections @Inject constructor(
    private val directions: Map<String, ModuleDestination>
) {
    fun module(name: String): ModuleDestination = requireNotNull(directions[name])
}
