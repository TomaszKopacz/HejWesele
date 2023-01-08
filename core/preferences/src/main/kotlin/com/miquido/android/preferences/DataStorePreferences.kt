package com.miquido.android.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences as AndroidxPreferences

internal class DataStorePreferences(
    private val name: String,
    context: Context
) : Preferences {

    private val store: DataStore<AndroidxPreferences> = PreferenceDataStoreFactory
        .create { context.preferencesDataStoreFile(name) }

    override suspend fun keys(): Set<String> {
        return store.data.firstOrNull()?.asMap()?.keys
            ?.map { it.name }
            ?.toSet()
            .orEmpty()
    }

    override suspend fun <T : Any> contains(key: Key<T>): Boolean {
        return store.data.firstOrNull()?.contains(key.asPrefKey()) ?: false
    }

    override suspend fun <T : Any> get(key: Key<T>): T? {
        return store.data.firstOrNull()?.get(key.asPrefKey())
    }

    override fun <T : Any> observe(key: Key<T>): Flow<T?> {
        val prefsKey = key.asPrefKey()
        return store.data.map { it[prefsKey] }.distinctUntilChanged()
    }

    override suspend fun <T : Any> put(entry: Entry<T>) {
        store.updateData {
            val mutable = it.toMutablePreferences()
            mutable.put(entry.key.asPrefKey(), entry.value)
            mutable
        }
    }

    override suspend fun putAll(entries: Set<Entry<*>>) {
        store.updateData {
            val mutable = it.toMutablePreferences()
            entries.forEach { entry -> mutable.put(entry.key.asPrefKey(), entry.value) }
            mutable
        }
    }

    override suspend fun update(transform: suspend (MutableMap<Key<*>, Any?>) -> MutableMap<Key<*>, Any?>) {
        store.updateData {
            val mutable = it.toMutablePreferences()
            val current: MutableMap<Key<*>, Any?> = mutable.asMap()
                .mapKeys { (key, value) -> Key(value::class, key.name) }
                .toMutableMap()
            val change = transform(current)
            change.forEach { (key, value) -> mutable.put(key.asPrefKey(), value) }
            mutable
        }
    }

    override suspend fun clear() {
        store.updateData { emptyPreferences() }
    }

    private fun <T : Any> MutablePreferences.put(key: AndroidxPreferences.Key<T>, value: T?) {
        if (value == null) remove(key) else set(key, value)
    }

    @Suppress("unchecked_cast")
    private fun <T : Any> Key<T>.asPrefKey(): AndroidxPreferences.Key<T> = when (type) {
        Int::class -> intPreferencesKey(name)
        Long::class -> longPreferencesKey(name)
        Float::class -> floatPreferencesKey(name)
        Double::class -> doublePreferencesKey(name)
        Boolean::class -> booleanPreferencesKey(name)
        String::class -> stringPreferencesKey(name)
        Set::class -> stringSetPreferencesKey(name) // we won't eagerly check elements contents
        else -> throw IllegalArgumentException("Attempt to store unsupported data type ${type.java.canonicalName} with key $name")
    } as AndroidxPreferences.Key<T>
}
