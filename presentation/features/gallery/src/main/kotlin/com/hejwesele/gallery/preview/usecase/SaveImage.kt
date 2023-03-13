package com.hejwesele.gallery.preview.usecase

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.hejwesele.extensions.BitmapProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class SaveImage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bitmapProvider: BitmapProvider
) {

    suspend operator fun invoke(url: String): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImageForAndroidQAndLater(url)
            } else {
                saveImageForAndroidLowerThanQ(url)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageForAndroidQAndLater(url: String) {
        val contentResolver = context.contentResolver

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val directory = "${Environment.DIRECTORY_PICTURES}/$FOLDER_NAME"
        val fileName = UUID.randomUUID().toString() + JPG

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)
        }

        contentResolver.insert(collection, values)?.let { uri ->
            val stream = contentResolver.openOutputStream(uri)
            bitmapProvider.fromUrl(url).compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream)
            stream?.close()
        } ?: throw IOException("Failed to save image from url $url")
    }

    private fun saveImageForAndroidLowerThanQ(url: String) {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val fileName = UUID.randomUUID().toString() + JPG
        val image = File(directory, fileName)
        val stream = FileOutputStream(image)

        bitmapProvider.fromUrl(url).compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream)
        stream.close()
    }

    companion object {
        private const val FOLDER_NAME = "HejWesele"
        private const val MIME_TYPE = "image/jpg"
        private const val JPG = ".jpg"
        private const val COMPRESS_QUALITY = 100
    }
}
