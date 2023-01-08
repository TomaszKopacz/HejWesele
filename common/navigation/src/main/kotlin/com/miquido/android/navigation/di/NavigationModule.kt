package com.miquido.android.navigation.di

import com.miquido.android.navigation.Navigation
import com.miquido.android.navigation.NavigationImpl
import com.miquido.android.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindNavigation(impl: NavigationImpl): Navigation

    @Binds
    fun bindNavigator(impl: NavigationImpl): Navigator
}
