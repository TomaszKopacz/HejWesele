package com.miquido.android.datetimeformatter.di

import com.miquido.android.datetimeformatter.DateTimeFormatter
import com.miquido.android.datetimeformatter.DateTimeFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DateTimeFormatterModule {

    @Binds
    fun bindDateTimeFormatter(impl: DateTimeFormatterImpl): DateTimeFormatter
}
