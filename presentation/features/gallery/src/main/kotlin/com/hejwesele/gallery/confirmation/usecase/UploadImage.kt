package com.hejwesele.gallery.confirmation.usecase

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.CompressFormat.PNG
import com.hejwesele.galleries.GalleriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class UploadImage @Inject constructor(
    private val repository: GalleriesRepository
) {

    suspend operator fun invoke(
        location: String,
        folder: String,
        image: Bitmap,
        format: CompressFormat
    ): Result<String> = withContext(Dispatchers.IO) {
        val name = UUID.randomUUID().toString()
        val extension = getFileExtension(format)
        val path = "$location/$folder/$name$extension"
        val stream = ByteArrayOutputStream()

        val reducedImage = Bitmap.createScaledBitmap(image, IMAGE_RESOLUTION, IMAGE_RESOLUTION, false)

        reducedImage.compress(format, COMPRESS_QUALITY_PERCENT, stream)
        val bytes = stream.toByteArray()

        repository.uploadImage(path = path, bytes = bytes)
    }

    private fun getFileExtension(format: CompressFormat): String = when (format) {
        JPEG -> ".jpg"
        PNG -> ".png"
        else -> throw IllegalArgumentException("Only PNG and JPG formats are supported.")
    }

    companion object {
        private const val IMAGE_RESOLUTION = 1080
        private const val COMPRESS_QUALITY_PERCENT = 80
    }
}
