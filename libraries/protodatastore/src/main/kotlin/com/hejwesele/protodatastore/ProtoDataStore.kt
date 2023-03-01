package com.hejwesele.protodatastore

import android.content.Context
import androidx.datastore.dataStore
import com.hejwesele.result.CompletableResult
import com.hejwesele.result.GeneralError.ServiceError
import com.hejwesele.result.Result
import com.hejwesele.result.completed
import com.hejwesele.result.failed
import com.hejwesele.result.failure
import com.hejwesele.result.success
import kotlinx.coroutines.flow.first
import java.io.IOException
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

    suspend fun readData(): Result<T> {
        return try {
            success(context.dataStore.data.first())
        } catch (exception: IOException) {
            failure(ServiceError(exception))
        }
    }

    suspend fun writeData(transform: (T) -> T): CompletableResult {
        return try {
            context.dataStore.updateData(transform)
            completed()
        } catch (exception: IOException) {
            failed(ServiceError(exception))
        }
    }
}
