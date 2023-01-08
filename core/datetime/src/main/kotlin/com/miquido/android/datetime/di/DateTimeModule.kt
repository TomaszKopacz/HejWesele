package com.miquido.android.datetime.di

import com.miquido.android.datetime.Clock
import com.miquido.android.datetime.ClockImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DateTimeModule {

    @Binds
    fun bindClock(impl: ClockImpl): Clock
}
