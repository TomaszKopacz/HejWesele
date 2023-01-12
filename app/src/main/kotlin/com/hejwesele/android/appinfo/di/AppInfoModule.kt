package com.hejwesele.android.appinfo.di

import com.hejwesele.android.appinfo.AppInfo
import com.hejwesele.android.appinfo.AppInfoImpl
import com.hejwesele.android.appinfo.ClientName
import com.hejwesele.android.appinfo.ClientNameImpl
import com.hejwesele.android.appinfo.SystemInfo
import com.hejwesele.android.appinfo.SystemInfoImpl
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
