package com.miquido.android.workmanager.di

import com.miquido.android.workmanager.AndroidXWorkManager
import com.miquido.android.workmanager.WorkManager
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
