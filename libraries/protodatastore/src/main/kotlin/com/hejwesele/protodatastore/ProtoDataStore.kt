package com.hejwesele.protodatastore

import android.content.Context
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProtoDataStore<T : Any> @Inject constructor(
    specification: ProtoMessageSpecification<T>,
    fileName: String,
    private val context: Context
) {
    private val Context.dataStore by dataStore(
        serializer = specification.serializer,
        fileName = fileName
    )

    suspend fun readData(): Result<T> = runCatching {
        withContext(Dispatchers.IO) {
            context.dataStore.data.first()
        }
    }

    suspend fun writeData(transform: (T) -> T): Result<T> = runCatching {
        withContext(Dispatchers.IO) {
            context.dataStore.updateData(transform)
        }
    }
}
