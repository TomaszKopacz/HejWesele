package com.hejwesele.onboarding.di

import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.onboarding.destinations.OnboardingDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class OnboardingDestinationModule {
    @Provides
    @IntoMap
    @StringKey(FeatureModules.ONBOARDING)
    fun provideDestination(): ModuleDestination = DirectDestinationSpec(OnboardingDestination)
}
