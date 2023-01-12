package com.hejwesele.android.crashlytics

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import javax.inject.Inject

internal class FirebaseCrashlytics @Inject constructor() : Crashlytics {

    private val tracker by lazy { Firebase.crashlytics }

    override fun enable() {
        tracker.setCrashlyticsCollectionEnabled(true)
    }

    override fun disable() {
        tracker.setCrashlyticsCollectionEnabled(false)
    }

    override fun log(message: String) {
        if (!isFirebaseInitialized()) return
        tracker.log(message)
    }

    override fun reportException(exception: Throwable) {
        if (!isFirebaseInitialized() || exception.isUnreportable) return
        tracker.recordException(exception)
    }

    @Suppress("SwallowedException")
    private fun isFirebaseInitialized(): Boolean = try {
        Firebase.app
        true
    } catch (exception: IllegalStateException) {
        false
    }

    private val Throwable.isUnreportable: Boolean
        get() = javaClass.getAnnotation(Unreportable::class.java) != null
}
