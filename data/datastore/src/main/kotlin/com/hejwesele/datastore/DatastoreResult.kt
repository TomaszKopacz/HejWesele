package com.hejwesele.datastore

sealed class DatastoreResult<DATA : Any> {
    class Success<T : Any>(val value: T) : DatastoreResult<T>()
    class Failure<T : Any>(val exception: Exception) : DatastoreResult<T>()
}

class DocumentNotExistsException(private val documentId: String) : Exception() {
    override val message: String
        get() = "Expected document with id $documentId does not exist."
}

suspend fun <DATA1 : Any, DATA2 : Any> DatastoreResult<DATA1>.then(
    action: suspend (DATA1) -> DatastoreResult<DATA2>
): DatastoreResult<DATA2> {
    return when (this) {
        is DatastoreResult.Success -> action(value)
        is DatastoreResult.Failure -> DatastoreResult.Failure(exception)
    }
}

fun <DATA : Any, DATA2 : Any> DatastoreResult<DATA>.mapSuccess(
    mapper: (DATA) -> DATA2
): DatastoreResult<DATA2> {
    return when (this) {
        is DatastoreResult.Success -> DatastoreResult.Success(mapper(value))
        is DatastoreResult.Failure -> DatastoreResult.Failure(exception)
    }
}

fun <DATA : Any> DatastoreResult<DATA>.onSuccess(
    action: (DATA) -> Unit
): DatastoreResult<DATA> {
    return when (this) {
        is DatastoreResult.Success -> {
            action(value)
            this
        }
        is DatastoreResult.Failure -> DatastoreResult.Failure(exception)
    }
}
