package com.hejwesele.result

sealed class CompletableResult {
    object Completed : CompletableResult()
    class Failure(val error: GeneralError) : CompletableResult()
}

fun completed(): CompletableResult {
    return CompletableResult.Completed
}

fun failed(error: GeneralError): CompletableResult {
    return CompletableResult.Failure(error)
}

fun CompletableResult.onCompleted(
    action: () -> Unit
): CompletableResult {
    return when (this) {
        CompletableResult.Completed -> {
            action()
            this
        }
        is CompletableResult.Failure -> failed(error)
    }
}

fun CompletableResult.onError(
    onServiceError: ((String?, Exception?) -> Unit)? = null,
    onNotFoundError: ((String?) -> Unit)? = null,
    onNetworkError: ((String?) -> Unit)? = null,
    onUnknownError: ((String?) -> Unit)? = null,
    action: ((GeneralError) -> Unit)? = null
): CompletableResult {
    return when (this) {
        is CompletableResult.Completed -> this
        is CompletableResult.Failure -> {
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

fun <T : Any> CompletableResult.mapToResult(
    mapper: () -> T
): Result<T> {
    return when (this) {
        CompletableResult.Completed -> Result.Success(mapper())
        is CompletableResult.Failure -> Result.Failure(error)
    }
}
