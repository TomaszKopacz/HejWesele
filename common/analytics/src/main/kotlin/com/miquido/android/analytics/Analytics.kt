package com.miquido.android.analytics

import androidx.annotation.Size
import kotlin.reflect.KClass

interface Analytics {

    companion object {
        internal const val EVENT_NAME_LENGTH_LIMIT = 40L
        internal const val PROPERTY_NAME_LENGTH_LIMIT = 24L
        internal const val PROPERTY_VALUE_LENGTH_LIMIT = 36L
    }

    fun enable()
    fun disable()

    fun setUserId(userId: String)

    fun setUserProperty(
        @Size(min = 1L, max = PROPERTY_NAME_LENGTH_LIMIT) key: String,
        @Size(max = PROPERTY_VALUE_LENGTH_LIMIT) value: String?
    )

    fun logEvent(
        @Size(min = 1L, max = EVENT_NAME_LENGTH_LIMIT) name: String,
        params: Map<String, Any> = emptyMap()
    )

    fun logScreenView(name: String, clazz: KClass<Any>? = null)
}
