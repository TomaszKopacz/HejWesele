package com.hejwesele.realtimedatabase

import com.google.firebase.FirebaseException

sealed class FirebaseRealtimeDatabaseResult<T : Any> {
    class Success<S : Any>(val value: S) : FirebaseRealtimeDatabaseResult<S>()
    class Error<S : Any>(val exception: Exception) : FirebaseRealtimeDatabaseResult<S>()
}

internal fun <T : Any> success(value: T) =
    FirebaseRealtimeDatabaseResult.Success(value)

internal fun <T : Any> error(message: String) =
    FirebaseRealtimeDatabaseResult.Error<T>(FirebaseException(message))
