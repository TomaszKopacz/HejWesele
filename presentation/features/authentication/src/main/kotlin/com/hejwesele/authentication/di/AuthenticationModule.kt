package com.hejwesele.authentication.di

import com.hejwesele.authentication.AuthenticationNavigator
import com.hejwesele.authentication.AuthenticationNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
internal interface AuthenticationModule {

    @Binds
    fun bindNavigator(impl: AuthenticationNavigatorImpl): AuthenticationNavigator
}
