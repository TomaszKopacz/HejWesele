package com.hejwesele.android.navigation

import com.hejwesele.android.navigation.FeatureModules.AUTHENTICATION
import com.hejwesele.android.navigation.FeatureModules.CONFIGURATION
import com.hejwesele.android.navigation.FeatureModules.DASHBOARD
import com.hejwesele.android.navigation.FeatureModules.GALLERY
import com.hejwesele.android.navigation.FeatureModules.HOME
import com.hejwesele.android.navigation.FeatureModules.ONBOARDING
import com.hejwesele.android.navigation.FeatureModules.SCHEDULE
import com.hejwesele.android.navigation.FeatureModules.SERVICES
import com.hejwesele.android.navigation.FeatureModules.SETTINGS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleDestinations @Inject constructor(
    private val directions: Map<String, @JvmSuppressWildcards ModuleDestinationSpec<*>>
) {

    fun auth(): DirectionModuleDestination = module(AUTHENTICATION)
    fun configuration(): DirectionModuleDestination = module(CONFIGURATION)
    fun onboarding(): DirectionModuleDestination = module(ONBOARDING)
    fun dashboard(): DirectionModuleDestination = module(DASHBOARD)
    fun settings(): DirectionModuleDestination = module(SETTINGS)
    fun home(): DirectionModuleDestination = module(HOME)
    fun schedule(): DirectionModuleDestination = module(SCHEDULE)
    fun services(): DirectionModuleDestination = module(SERVICES)
    fun gallery(): DirectionModuleDestination = module(GALLERY)

    @Suppress("UNCHECKED_CAST")
    private fun <T> module(name: String): ModuleDestinationSpec<T> =
        requireNotNull(directions[name]) as ModuleDestinationSpec<T>
}