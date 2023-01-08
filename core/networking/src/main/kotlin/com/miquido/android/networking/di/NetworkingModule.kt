package com.miquido.android.networking.di

import com.miquido.android.networking.ApiProvider
import com.miquido.android.networking.RetrofitApiProvider
import com.miquido.android.networking.UserAgentInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkingModule {

    @Binds
    @Singleton
    fun bindApiProvider(impl: RetrofitApiProvider): ApiProvider

    @Binds
    @Singleton
    fun bindInterceptor(impl: UserAgentInterceptor): Interceptor
}
