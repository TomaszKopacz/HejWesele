package com.hejwesele.datastore

import android.content.Context
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataStore<T : Any> @Inject constructor(
    specification: MessageSpecification<T>,
    fileName: String,
    private val context: Context
) {
    private val Context.dataStoreReference by dataStore(
        serializer = specification.serializer,
        fileName = fileName
    )

    suspend fun readData(): Result<T> = runCatching {
        withContext(Dispatchers.IO) {
            context.dataStoreReference.data.first()
        }
    }

    fun observeData(): Flow<T> = context.dataStoreReference.data.flowOn(Dispatchers.IO)

    suspend fun writeData(transform: (T) -> T): Result<T> = runCatching {
        withContext(Dispatchers.IO) {
            context.dataStoreReference.updateData(transform)
        }
    }
}
