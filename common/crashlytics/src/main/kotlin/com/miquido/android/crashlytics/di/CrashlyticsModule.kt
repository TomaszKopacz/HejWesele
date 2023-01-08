package com.miquido.android.crashlytics.di

import com.miquido.android.crashlytics.Crashlytics
import com.miquido.android.crashlytics.FirebaseCrashlytics
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface CrashlyticsModule {

    @Binds
    @Singleton
    fun bindCrashlytics(impl: FirebaseCrashlytics): Crashlytics
}
