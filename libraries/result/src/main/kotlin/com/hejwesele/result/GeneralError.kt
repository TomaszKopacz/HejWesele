package com.hejwesele.result

open class GeneralError(
    override val message: String?
) : Throwable() {
    class ServiceError(message: String? = null) : GeneralError(message)
    class NotFoundError(message: String? = null) : GeneralError(message)
    class NetworkError(message: String? = null) : GeneralError(message)
    class UnknownError(message: String? = null) : GeneralError(message)
}

fun serviceError(message: String? = null) = GeneralError.ServiceError(message)

fun notFoundError(message: String? = null) = GeneralError.NotFoundError(message)

fun networkError(message: String? = null) = GeneralError.NetworkError(message)

fun unknownError(message: String? = null) = GeneralError.UnknownError(message)

fun notFound(name: String, id: String) = serviceError("Expected $name with id $id not found.")
