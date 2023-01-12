package com.hejwesele.android.networking.di

import com.hejwesele.android.networking.ApiProvider
import com.hejwesele.android.networking.RetrofitApiProvider
import com.hejwesele.android.networking.UserAgentInterceptor
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
