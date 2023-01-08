package com.miquido.android.thememanager

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import javax.inject.Inject

internal interface AppCompatDelegateWrapper {
    fun setDefaultNightMode(@NightMode mode: Int)
}

internal class AppCompatDelegateWrapperImpl @Inject constructor() : AppCompatDelegateWrapper {

    override fun setDefaultNightMode(@NightMode mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
