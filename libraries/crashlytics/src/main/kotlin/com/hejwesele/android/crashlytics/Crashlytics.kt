package com.hejwesele.android.crashlytics

interface Crashlytics {

    fun enable()
    fun disable()

    fun log(message: String)
    fun reportException(exception: Throwable)
}
