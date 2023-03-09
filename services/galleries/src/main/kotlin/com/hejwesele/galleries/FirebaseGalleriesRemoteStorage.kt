package com.hejwesele.galleries

import com.hejwesele.remotestorage.FirebaseRemoteStorage
import javax.inject.Inject

class FirebaseGalleriesRemoteStorage @Inject constructor(
    private val storage: FirebaseRemoteStorage
) : GalleriesRemoteStorage {

    override suspend fun uploadImage(path: String, bytes: ByteArray): Result<String> {
        return storage.uploadImage(path, bytes)
    }
}
