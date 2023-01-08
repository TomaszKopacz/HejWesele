package com.miquido.android.appinfo.di

import com.miquido.android.appinfo.AppInfo
import com.miquido.android.appinfo.AppInfoImpl
import com.miquido.android.appinfo.ClientName
import com.miquido.android.appinfo.ClientNameImpl
import com.miquido.android.appinfo.SystemInfo
import com.miquido.android.appinfo.SystemInfoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AppInfoModule {

    @Binds
    @Singleton
    fun bindAppInfo(impl: AppInfoImpl): AppInfo

    @Binds
    @Singleton
    fun bindClientName(impl: ClientNameImpl): ClientName

    @Binds
    @Singleton
    fun bindSystemInfo(impl: SystemInfoImpl): SystemInfo
}
