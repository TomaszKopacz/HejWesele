package com.hejwesele.home.di

import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.home.destinations.HomeDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class HomeDestinationModule {

//    @Provides
//    @StartModuleDestination
//    fun provideStart(): DirectionModuleDestination = DirectDestinationSpec(HomeDestination)

    @Provides
    @IntoMap
    @StringKey(FeatureModules.HOME)
    fun provideDestination(): ModuleDestination = DirectDestinationSpec(HomeDestination)
}
