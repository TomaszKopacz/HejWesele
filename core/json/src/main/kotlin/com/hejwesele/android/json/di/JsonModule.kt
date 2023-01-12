package com.hejwesele.android.json.di

import com.hejwesele.android.json.Json
import com.hejwesele.android.json.MoshiJson
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface JsonModule {

    @Binds
    @Singleton
    fun bindJson(impl: MoshiJson): Json
}
