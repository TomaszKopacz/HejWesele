package com.hejwesele.galleries

import com.hejwesele.remotestorage.FirebaseRemoteStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseGalleriesRemoteStorage @Inject constructor(
    private val storage: FirebaseRemoteStorage
) : GalleriesRemoteStorage {

    override suspend fun uploadImage(path: String, bytes: ByteArray): Result<String> = withContext(Dispatchers.IO) {
        storage.uploadImage(path, bytes)
    }
}
