package com.hejwesele.android.osinfo.di

import com.hejwesele.android.osinfo.AndroidOsInfo
import com.hejwesele.android.osinfo.OsInfo
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
