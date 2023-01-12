package com.hejwesele.android.remoteconfig.di

import com.hejwesele.android.remoteconfig.FirebaseRemoteConfig
import com.hejwesele.android.remoteconfig.RemoteConfig
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
