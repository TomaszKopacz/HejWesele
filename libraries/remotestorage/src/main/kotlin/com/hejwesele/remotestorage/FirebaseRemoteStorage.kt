package com.hejwesele.remotestorage

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class FirebaseRemoteStorage @Inject constructor() {
    private val storage = Firebase
        .storage
        .apply {
            maxUploadRetryTimeMillis = OPERATION_TIMEOUT_MS
            maxDownloadRetryTimeMillis = OPERATION_TIMEOUT_MS
            maxOperationRetryTimeMillis = OPERATION_TIMEOUT_MS
        }.reference


    suspend fun uploadImage(path: String, bytes: ByteArray): Result<String> = suspendCoroutine { continuation ->
        val reference = storage.child(IMAGES).child(path)

        try {
            reference
                .putBytes(bytes)
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
        } catch (exception: StorageException) {
            continuation.resume(failure(exception))
        }
    }

    companion object {
        private const val IMAGES = "images/"
        private const val OPERATION_TIMEOUT_MS = 5000L

    }
}
