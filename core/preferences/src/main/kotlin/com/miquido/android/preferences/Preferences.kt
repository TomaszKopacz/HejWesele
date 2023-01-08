package com.miquido.android.preferences

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

data class Key<out T : Any>(val type: KClass<out T>, val name: String) {
    infix fun <T : Any> to(value: T?) = Entry(this, value)
}

data class Entry<out T : Any> internal constructor(val key: Key<T>, val value: T?)

interface Preferences {
    suspend fun keys(): Set<String>
    suspend fun <T : Any> contains(key: Key<T>): Boolean
    suspend fun <T : Any> get(key: Key<T>): T?
    fun <T : Any> observe(key: Key<T>): Flow<T?>
    suspend fun <T : Any> put(entry: Entry<T>)
    suspend fun putAll(entries: Set<Entry<*>>)
    suspend fun update(transform: suspend (MutableMap<Key<*>, Any?>) -> MutableMap<Key<*>, Any?>)
    suspend fun clear()

    companion object Files {
        const val DEFAULT = "default"
    }
}

inline fun <reified T : Any> key(name: String) = Key(T::class, name)
infix fun String.to(value: Int?) = Key(Int::class, this) to value
infix fun String.to(value: Long?) = Key(Long::class, this) to value
infix fun String.to(value: Float?) = Key(Float::class, this) to value
infix fun String.to(value: Double?) = Key(Double::class, this) to value
infix fun String.to(value: Boolean?) = Key(Boolean::class, this) to value
infix fun String.to(value: String?) = Key(String::class, this) to value
infix fun String.to(value: Set<String>?) = Key(Set::class, this) to value
