package com.hejwesele.android.crashlytics.di

import com.hejwesele.android.crashlytics.Crashlytics
import com.hejwesele.android.crashlytics.FirebaseCrashlytics
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
