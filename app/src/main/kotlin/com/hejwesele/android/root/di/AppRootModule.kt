package com.hejwesele.android.root.di

import com.hejwesele.android.root.AppRootProvider
import com.hejwesele.android.root.AppRootProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal interface AppRootModule {

    @Binds
    fun bindAppRootProvider(impl: AppRootProviderImpl): AppRootProvider
}