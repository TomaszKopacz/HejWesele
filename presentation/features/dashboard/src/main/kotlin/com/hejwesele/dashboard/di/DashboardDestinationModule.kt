package com.hejwesele.dashboard.di

import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.dashboard.destinations.DashboardDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class DashboardDestinationModule {
    @Provides
    @IntoMap
    @StringKey(FeatureModules.DASHBOARD)
    fun provideDestination(): ModuleDestination = DirectDestinationSpec(DashboardDestination)
}
