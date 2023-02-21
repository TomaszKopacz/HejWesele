package com.hejwesele.schedule.di

import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.schedule.destinations.ScheduleDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class ScheduleDestinationModule {
    @Provides
    @IntoMap
    @StringKey(FeatureModules.SCHEDULE)
    fun provideDestination(): ModuleDestination = DirectDestinationSpec(ScheduleDestination)
}
