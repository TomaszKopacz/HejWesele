package com.hejwesele.realtimedatabase

import com.google.firebase.FirebaseException

sealed class FirebaseResult<T> {
    class Success<S>(val value: S) : FirebaseResult<S>()
    class Error<S>(val exception: Exception) : FirebaseResult<S>()
    class NoSuchItem<S> : FirebaseResult<S>()
}

internal fun <T> success(value: T) =
    FirebaseResult.Success(value)

internal fun <T> error(message: String) =
    FirebaseResult.Error<T>(FirebaseException(message))

internal fun <T> noItem() = FirebaseResult.NoSuchItem<T>()
