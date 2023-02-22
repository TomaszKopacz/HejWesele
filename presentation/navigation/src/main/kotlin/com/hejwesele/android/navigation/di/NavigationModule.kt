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
abstract class NavigationModule {

    @Binds
    internal abstract fun bindNavigation(impl: NavigationImpl): Navigation

    @Binds
    internal abstract fun bindNavigator(impl: NavigationImpl): Navigator
}
