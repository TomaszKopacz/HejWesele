package com.hejwesele.result

import kotlin.Exception

open class GeneralError(
    val exception: Exception?,
    val message: String?
) {
    class ServiceError(exception: Exception? = null, message: String? = null) : GeneralError(exception, message)
    class NotFoundError(message: String? = null) : GeneralError(null, message)
    class NetworkError(message: String? = null) : GeneralError(null, message)
    class UnknownError(message: String? = null) : GeneralError(null, message)
}

fun serviceError(exception: Exception? = null, message: String? = null) = GeneralError.ServiceError(exception, message)

fun notFoundError(message: String? = null) = GeneralError.NotFoundError(message)

fun networkError(message: String? = null) = GeneralError.NetworkError(message)

fun unknownError(message: String? = null) = GeneralError.UnknownError(message)

