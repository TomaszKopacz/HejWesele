package com.miquido.android.remoteconfig

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.VALUE_SOURCE_STATIC
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject

internal class FirebaseRemoteConfig @Inject constructor() : RemoteConfig {

    private val config by lazy { Firebase.remoteConfig }

    override fun init() {
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = defaultExpiration
        }
        config.setConfigSettingsAsync(settings)
            .continueWith { config.fetchAndActivate() }
    }

    override fun fetch(expiration: Long) {
        config.fetch(expiration)
            .continueWith { config.activate() }
    }

    override fun getString(key: String, default: String) = getValue(key)?.asString() ?: default
    override fun getLong(key: String, default: Long) = getValue(key)?.asLong() ?: default
    override fun getBoolean(key: String, default: Boolean) = getValue(key)?.asBoolean() ?: default

    override fun getStringOrNull(key: String) = getValue(key)?.asString()
    override fun getLongOrNull(key: String) = getValue(key)?.asLong()
    override fun getBooleanOrNull(key: String) = getValue(key)?.asBoolean()

    private fun getValue(key: String): FirebaseRemoteConfigValue? {
        return config[key].takeIf { it.source != VALUE_SOURCE_STATIC }
    }
}
