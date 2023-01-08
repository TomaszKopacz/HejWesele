package com.miquido.android.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.lang.reflect.Type
import java.util.Date
import javax.inject.Inject

internal class MoshiJson @Inject constructor() : Json {

    private val moshi by lazy {
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }

    override fun <T> toJson(value: T, type: Type): String {
        return moshi.adapter<T>(type).lenient().toJson(value)
    }

    override fun <T> fromJson(json: String, type: Type): T? {
        return moshi.adapter<T>(type).lenient().fromJson(json)
    }
}
