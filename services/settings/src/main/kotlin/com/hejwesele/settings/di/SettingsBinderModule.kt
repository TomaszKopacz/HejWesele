package com.hejwesele.settings.di

import com.hejwesele.settings.ProtoDataStoreSettingsLocalSource
import com.hejwesele.settings.SettingsLocalSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface SettingsBinderModule {

    @Binds
    fun bindSettingsLocalSource(impl: ProtoDataStoreSettingsLocalSource): SettingsLocalSource
}
