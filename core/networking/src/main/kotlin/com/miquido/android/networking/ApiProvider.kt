package com.miquido.android.networking

interface ApiProvider {
    fun <T> provide(url: String, type: Class<T>): T
}

inline fun <reified T> ApiProvider.provide(url: String) = provide(url, T::class.java)
