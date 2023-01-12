package com.hejwesele.android.workmanager.di

import com.hejwesele.android.workmanager.AndroidXWorkManager
import com.hejwesele.android.workmanager.WorkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface WorkManagerModule {

    @Binds
    @Singleton
    fun bindWorkManager(impl: AndroidXWorkManager): WorkManager
}
