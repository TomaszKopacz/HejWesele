package com.hejwesele.result.extensions

import com.hejwesele.result.unknownError
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

suspend fun <T, R> Result<T>.flatMap(
    mapper: suspend (T) -> Result<R>
): Result<R> {
    return when {
        isSuccess -> mapper(getOrThrow())
        else -> failure(exceptionOrNull() ?: unknownError())
    }
}

fun <T, R, N> Result<T>.merge(other: Result<R>, transform: (T, R) -> N): Result<N> {
    return when {
        isFailure -> failure(exceptionOrNull() ?: IllegalStateException(ExceptionMessage.EXCEPTION_NOT_DEFINED))
        other.isFailure -> failure(other.exceptionOrNull() ?: IllegalStateException(ExceptionMessage.EXCEPTION_NOT_DEFINED))
        isSuccess && other.isSuccess -> success(transform(getOrThrow(), other.getOrThrow()))
        else -> failure(IllegalStateException(ExceptionMessage.EXCEPTION_NOT_DEFINED))
    }
}

fun <T, R> Result<T>.mergeToPair(other: Result<R>): Result<Pair<T, R>> {
    return merge(other) { first, second -> first to second }
}

private object ExceptionMessage {
    const val EXCEPTION_NOT_DEFINED = "Failure but exception not defined."
}
