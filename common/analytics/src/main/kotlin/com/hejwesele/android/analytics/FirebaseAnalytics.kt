package com.hejwesele.android.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event.SCREEN_VIEW
import com.google.firebase.analytics.FirebaseAnalytics.Param.SCREEN_CLASS
import com.google.firebase.analytics.FirebaseAnalytics.Param.SCREEN_NAME
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.hejwesele.android.analytics.Analytics.Companion.EVENT_NAME_LENGTH_LIMIT
import com.hejwesele.android.analytics.Analytics.Companion.PROPERTY_NAME_LENGTH_LIMIT
import javax.inject.Inject
import kotlin.reflect.KClass

internal class FirebaseAnalytics @Inject constructor() : Analytics {

    private val analytics: FirebaseAnalytics by lazy {
        Firebase.analytics.apply {
            setAnalyticsCollectionEnabled(false)
        }
    }

    override fun enable() {
        analytics.setAnalyticsCollectionEnabled(true)
    }

    override fun disable() {
        analytics.setAnalyticsCollectionEnabled(false)
    }

    override fun setUserId(userId: String) {
        analytics.setUserId(userId)
    }

    override fun setUserProperty(key: String, value: String?) {
        require(key.length in 1..PROPERTY_NAME_LENGTH_LIMIT)
        if (value != null) require(value.length in 1..PROPERTY_NAME_LENGTH_LIMIT)
        analytics.setUserProperty(key, value)
    }

    override fun logEvent(name: String, params: Map<String, Any>) {
        require(name.length in 1..EVENT_NAME_LENGTH_LIMIT)
        analytics.logEvent(name, prepareBundle(params))
    }

    override fun logScreenView(name: String, clazz: KClass<Any>?) {
        analytics.logEvent(SCREEN_VIEW) {
            param(SCREEN_NAME, name)
            val className = clazz?.simpleName
            if (className != null) param(SCREEN_CLASS, className)
        }
    }

    private fun prepareBundle(parameters: Map<String, Any>): Bundle =
        Bundle().apply {
            parameters.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Double -> putDouble(key, value)
                    is Float -> putFloat(key, value)
                    is Long -> putLong(key, value)
                    else -> throw IllegalArgumentException("$value type not supported as analytics value")
                }
            }
        }
}
