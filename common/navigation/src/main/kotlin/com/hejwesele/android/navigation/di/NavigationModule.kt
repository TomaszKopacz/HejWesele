package com.hejwesele.android.navigation.di

import com.hejwesele.android.navigation.Navigation
import com.hejwesele.android.navigation.NavigationImpl
import com.hejwesele.android.navigation.Navigator
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
