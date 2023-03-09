package com.hejwesele.remotestorage

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRemoteStorage @Inject constructor() {
    private val storage = Firebase.storage.reference

    suspend fun uploadImage(path: String, bytes: ByteArray): Result<String> = suspendCoroutine { continuation ->
        val reference = storage.child(IMAGES).child(path)

        reference.putBytes(bytes)
            .addOnSuccessListener {
                reference.downloadUrl
                    .addOnSuccessListener { url ->
                        continuation.resume(success(url.toString()))
                    }
                    .addOnFailureListener { error ->
                        continuation.resume(failure(error))
                    }
            }
            .addOnFailureListener { error ->
                continuation.resume(failure(error))
            }
    }

    companion object {
        private const val IMAGES = "images/"
    }
}
