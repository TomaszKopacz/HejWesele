package com.hejwesele.galleries

interface GalleriesRemoteStorage {

    suspend fun uploadImage(path: String, bytes: ByteArray): Result<String>
}
