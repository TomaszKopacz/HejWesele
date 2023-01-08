package com.miquido.android.remoteconfig.di

import com.miquido.android.remoteconfig.FirebaseRemoteConfig
import com.miquido.android.remoteconfig.RemoteConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RemoteConfigModule {

    @Binds
    @Singleton
    fun bindRemoteConfig(impl: FirebaseRemoteConfig): RemoteConfig
}
