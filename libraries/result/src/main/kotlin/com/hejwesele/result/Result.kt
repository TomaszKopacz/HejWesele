package com.hejwesele.result

sealed class Result<DATA : Any> {
    class Success<T : Any>(val value: T) : Result<T>()
    class Failure<T : Any>(val error: GeneralError) : Result<T>()
}

fun <DATA : Any> success(value: DATA): Result<DATA> {
    return Result.Success(value)
}

fun <DATA : Any> failure(error: GeneralError): Result<DATA> {
    return Result.Failure(error)
}

suspend fun <DATA : Any> Result<DATA>.onSuccess(
    action: suspend (DATA) -> Unit
): Result<DATA> {
    return when (this) {
        is Result.Success -> {
            action(value)
            this
        }
        is Result.Failure -> failure(error)
    }
}

suspend fun <T : Any, S : Any> Result<T>.mapSuccess(
    mapper: suspend (T) -> S
): Result<S> {
    return when (this) {
        is Result.Success -> success(mapper(value))
        is Result.Failure -> failure(error)
    }
}

suspend fun <T : Any> Result<T>.mapError(
    mapper: suspend (GeneralError) -> GeneralError
): Result<T> {
    return when (this) {
        is Result.Success -> this
        is Result.Failure -> failure(mapper(error))
    }
}

suspend fun <T : Any, S : Any> Result<T>.then(
    action: suspend (T) -> Result<S>
): Result<S> {
    return when (this) {
        is Result.Success -> action(value)
        is Result.Failure -> failure(error)
    }
}

suspend fun <T : Any> Result<T>.thenCompletable(
    action: suspend (T) -> CompletableResult
): CompletableResult {
    return when (this) {
        is Result.Success -> action(value)
        is Result.Failure -> failed(error)
    }
}

fun <T : Any, S : Any> Result<T>.flatMapSuccess(
    mapper: (T) -> Result<S>
): Result<S> {
    return when (this) {
        is Result.Success -> mapper(value)
        is Result.Failure -> failure(error)
    }
}

fun <DATA : Any> Result<DATA>.onError(
    onServiceError: ((String?, Exception?) -> Unit)? = null,
    onNotFoundError: ((String?) -> Unit)? = null,
    onNetworkError: ((String?) -> Unit)? = null,
    onUnknownError: ((String?) -> Unit)? = null,
    action: ((GeneralError) -> Unit)? = null
): Result<DATA> {
    return when (this) {
        is Result.Success -> this
        is Result.Failure -> {
            action?.invoke(error)
            when (error) {
                is GeneralError.ServiceError -> onServiceError?.invoke(error.message, error.exception)
                is GeneralError.NotFoundError -> onNotFoundError?.invoke(error.message)
                is GeneralError.NetworkError -> onNetworkError?.invoke(error.message)
                is GeneralError.UnknownError -> onUnknownError?.invoke(error.message)
            }
            this
        }
    }
}
