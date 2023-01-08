package com.miquido.android.analytics.di

import com.miquido.android.analytics.Analytics
import com.miquido.android.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AnalyticsModule {

    @Binds
    @Singleton
    fun bindAnalytics(impl: FirebaseAnalytics): Analytics
}
