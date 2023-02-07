package com.hejwesele.result

import java.lang.Exception

sealed class GeneralError {
    class ServiceError(val message: String? = null, val exception: Exception? = null) : GeneralError()
    class NetworkError(val message: String) : GeneralError()
    class UnknownError(val message: String) : GeneralError()
}

fun serviceError(message: String? = null, exception: Exception? = null) =
    GeneralError.ServiceError(message, exception)
