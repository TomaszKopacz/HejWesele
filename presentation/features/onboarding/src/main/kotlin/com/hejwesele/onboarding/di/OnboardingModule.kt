package com.hejwesele.onboarding.di

import com.hejwesele.onboarding.OnboardingRepository
import com.hejwesele.onboarding.OnboardingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class OnboardingModule {
    @Provides
    @Singleton
    fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository = impl
}
