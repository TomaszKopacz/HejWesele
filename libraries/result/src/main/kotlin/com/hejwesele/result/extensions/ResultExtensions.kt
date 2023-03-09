package com.hejwesele.result.extensions

suspend fun <T, R> Result<T>.flatMap(
    mapper: suspend (T) -> Result<R>
): Result<R> {
    return mapper(getOrThrow())
}
