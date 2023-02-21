package com.hejwesele.authentication.di

import com.hejwesele.android.navigation.DirectionModuleDestination
import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.android.navigation.StartModuleDestination
import com.hejwesele.authentication.destinations.AuthenticationDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class AuthenticationDestinationModule {
    private val auth = DirectDestinationSpec(AuthenticationDestination)

    @Provides
    @StartModuleDestination
    fun provideStart(): DirectionModuleDestination = auth

    @Provides
    @IntoMap
    @StringKey(FeatureModules.AUTHENTICATION)
    fun provideDestination(): ModuleDestination = auth
}
