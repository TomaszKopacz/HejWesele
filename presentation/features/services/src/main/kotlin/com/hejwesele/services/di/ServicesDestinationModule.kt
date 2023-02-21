package com.hejwesele.services.di

import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.services.destinations.ServicesDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class ServicesDestinationModule {
    @Provides
    @IntoMap
    @StringKey(FeatureModules.SERVICES)
    fun provideDestination(): ModuleDestination = DirectDestinationSpec(ServicesDestination)
}
