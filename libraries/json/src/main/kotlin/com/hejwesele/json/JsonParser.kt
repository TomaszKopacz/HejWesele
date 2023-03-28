package com.hejwesele.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type
import javax.inject.Inject

class JsonParser @Inject constructor() {
    private val moshi by lazy {
        Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    fun <T : Any> toJson(data: T, type: Type): Result<String> = runCatching {
        moshi.adapter<T>(type).lenient().toJson(data)
    }

    fun <T : Any> fromJson(json: String, type: Type): Result<T?> = runCatching {
        moshi.adapter<T>(type).lenient().fromJson(json)
    }
}
