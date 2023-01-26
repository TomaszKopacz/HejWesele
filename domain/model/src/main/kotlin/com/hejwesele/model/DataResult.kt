package com.hejwesele.model

import com.hejwesele.model.DataResult.Failure
import com.hejwesele.model.DataResult.Success
import com.hejwesele.model.GeneralError.DatastoreError
import com.hejwesele.model.GeneralError.MissingDataError
import com.hejwesele.model.GeneralError.NetworkError
import com.hejwesele.model.GeneralError.UnknownError

sealed class DataResult<DATA : Any> {
    class Success<T : Any>(val value: T) : DataResult<T>()
    class Failure<T : Any>(val error: GeneralError) : DataResult<T>()
}

fun <DATA : Any> success(value: DATA): DataResult<DATA> {
    return Success(value)
}

fun <DATA : Any> failure(error: GeneralError): DataResult<DATA> {
    return Failure(error)
}

fun <DATA : Any> DataResult<DATA>.onSuccess(
    action: (DATA) -> Unit
): DataResult<DATA> {
    return when (this) {
        is Success -> {
            action(value)
            this
        }
        is Failure -> Failure(error)
    }
}

fun <DATA : Any> DataResult<DATA>.onError(
    onDatastoreError: ((String?) -> Unit)? = null,
    onMissingDataError: ((String?) -> Unit)? = null,
    onNetworkError: ((String?) -> Unit)? = null,
    onUnknownError: ((String?) -> Unit)? = null,
    action: ((GeneralError) -> Unit)? = null
): DataResult<DATA> {
    return when (this) {
        is Success -> this
        is Failure -> {
            action?.invoke(error)
            when (error) {
                is DatastoreError -> onDatastoreError?.invoke(error.message)
                is MissingDataError -> onMissingDataError?.invoke(error.message)
                is NetworkError -> onNetworkError?.invoke(error.message)
                is UnknownError -> onUnknownError?.invoke(error.message)
            }
            this
        }
    }
}
