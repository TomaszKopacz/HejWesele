package com.hejwesele.android.json

import java.lang.reflect.Type

interface Json {
    fun <T> toJson(value: T, type: Type): String
    fun <T> fromJson(json: String, type: Type): T?
}

inline fun <reified T> Json.toJson(value: T) = toJson(value, T::class.java)
inline fun <reified T> Json.fromJson(json: String): T? = fromJson(json, T::class.java)
