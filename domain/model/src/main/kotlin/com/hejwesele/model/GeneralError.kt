package com.hejwesele.model

sealed class GeneralError {
    class DatastoreError(val message: String?) : GeneralError()
    class MissingDataError(val message: String) : GeneralError()
    class NetworkError(val message: String) : GeneralError()
    class UnknownError(val message: String) : GeneralError()
}
