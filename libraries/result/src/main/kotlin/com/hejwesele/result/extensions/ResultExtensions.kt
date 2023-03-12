package com.hejwesele.result.extensions

import com.hejwesele.result.unknownError

suspend fun <T, R> Result<T>.flatMap(
    mapper: suspend (T) -> Result<R>
): Result<R> {
    return when {
        isSuccess -> mapper(getOrThrow())
        else -> Result.failure(exceptionOrNull() ?: unknownError())
    }
}
