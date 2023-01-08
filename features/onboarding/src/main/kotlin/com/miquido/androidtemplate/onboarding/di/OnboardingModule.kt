package com.miquido.androidtemplate.onboarding.di

import com.miquido.androidtemplate.onboarding.OnboardingRepository
import com.miquido.androidtemplate.onboarding.OnboardingRepositoryImpl
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
