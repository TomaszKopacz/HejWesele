package com.hejwesele.android.config.di

import com.hejwesele.android.config.switcher.destinations.ConfigurationScreenDestination
import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class ConfigurationDestinationModule {
    @Provides
    @IntoMap
    @StringKey(FeatureModules.CONFIGURATION)
    fun provideDestination(): ModuleDestination = DirectDestinationSpec(ConfigurationScreenDestination)
}
