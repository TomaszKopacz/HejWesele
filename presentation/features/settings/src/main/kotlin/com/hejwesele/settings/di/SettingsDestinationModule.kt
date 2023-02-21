package com.hejwesele.settings.di

import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.NestedDestinationSpec
import com.hejwesele.settings.SettingsNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class SettingsDestinationModule {
    @Provides
    @IntoMap
    @StringKey(FeatureModules.SETTINGS)
    fun provideDestination(): ModuleDestination = NestedDestinationSpec(SettingsNavGraph)
}
