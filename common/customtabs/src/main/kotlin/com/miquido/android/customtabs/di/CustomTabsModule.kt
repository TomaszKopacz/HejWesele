package com.miquido.android.customtabs.di

import com.miquido.android.customtabs.AndroidXCustomTabs
import com.miquido.android.customtabs.CustomTabs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
internal interface CustomTabsModule {

    @Binds
    @ActivityScoped
    fun bindCustomTabs(impl: AndroidXCustomTabs): CustomTabs
}
