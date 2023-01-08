package com.miquido.android.remoteconfig

import com.miquido.androidtemplate.remoteconfig.BuildConfig

interface RemoteConfig {

    companion object {
        private const val DEFAULT_EXPIRATION = 3600L // 1h in seconds
    }

    val defaultExpiration: Long
        get() = if (BuildConfig.DEBUG) 0 else DEFAULT_EXPIRATION

    fun init()
    fun fetch(expiration: Long = defaultExpiration)

    fun getString(key: String, default: String): String
    fun getLong(key: String, default: Long): Long
    fun getBoolean(key: String, default: Boolean): Boolean

    fun getStringOrNull(key: String): String?
    fun getLongOrNull(key: String): Long?
    fun getBooleanOrNull(key: String): Boolean?
}
