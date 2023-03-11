package com.hejwesele.gallery.confirmation.usecase

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.CompressFormat.PNG
import com.hejwesele.galleries.GalleriesRepository
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class UploadImage @Inject constructor(
    private val repository: GalleriesRepository
) {

    suspend operator fun invoke(
        galleryId: String,
        bitmap: Bitmap,
        format: CompressFormat
    ): Result<String> {
        val name = UUID.randomUUID().toString()
        val extension = getFileExtension(format)
        val path = "$GALLERY_FOLDER_NAME/$galleryId/$name$extension"
        val stream = ByteArrayOutputStream()

        bitmap.compress(format, COMPRESS_QUALITY_PERCENT, stream)
        val bytes = stream.toByteArray()

        return repository.uploadImage(path = path, bytes = bytes)
    }

    private fun getFileExtension(format: CompressFormat): String = when (format) {
        JPEG -> ".jpg"
        PNG -> ".png"
        else -> throw IllegalArgumentException("Only PNG and JPG formats are supported.")
    }

    companion object {
        private const val COMPRESS_QUALITY_PERCENT = 100
        private const val GALLERY_FOLDER_NAME = "gallery"
    }
}
