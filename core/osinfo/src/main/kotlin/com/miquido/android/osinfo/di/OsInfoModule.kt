package com.miquido.android.osinfo.di

import com.miquido.android.osinfo.AndroidOsInfo
import com.miquido.android.osinfo.OsInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface OsInfoModule {

    @Binds
    fun bindOsInfo(impl: AndroidOsInfo): OsInfo
}
