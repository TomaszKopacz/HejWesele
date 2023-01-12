package com.hejwesele.android.database

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.SecureRandom
import javax.inject.Inject

internal class PassphraseProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PASSPHRASE_LENGTH = 32
    }

    private val masterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(AES256_GCM)
            .build()
    }

    fun providePassphrase(name: String): ByteArray {
        val file = File(context.filesDir, "$name.pp")
        return if (file.exists()) retrievePassphrase(file) else createPassphrase(file)
    }

    private fun createPassphrase(file: File): ByteArray {
        val random = SecureRandom()
        val bytes = ByteArray(PASSPHRASE_LENGTH)
        random.nextBytes(bytes)
        savePassphrase(file, bytes)
        return bytes
    }

    private fun savePassphrase(file: File, bytes: ByteArray) {
        val encryptedFile = file.encrypted()
        if (file.exists()) file.delete()
        encryptedFile.openFileOutput().use { output ->
            output.write(bytes)
            output.flush()
        }
    }

    private fun retrievePassphrase(file: File): ByteArray {
        val encryptedFile = file.encrypted()
        val output = ByteArrayOutputStream()
        encryptedFile.openFileInput().use { input ->
            var nextByte: Int = input.read()
            while (nextByte != -1) {
                output.write(nextByte)
                nextByte = input.read()
            }
        }
        return output.toByteArray()
    }

    private fun File.encrypted() = EncryptedFile
        .Builder(context, this, masterKey, AES256_GCM_HKDF_4KB)
        .build()
}
