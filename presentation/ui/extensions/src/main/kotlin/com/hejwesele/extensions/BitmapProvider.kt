package com.hejwesele.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BitmapProvider @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun fromUrl(url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val imageLoader = ImageLoader.Builder(context).build()
            val request = ImageRequest.Builder(context).data(url).build()
            val result = (imageLoader.execute(request) as SuccessResult).drawable

            (result as BitmapDrawable).bitmap
        }
    }

    @Suppress("DEPRECATION", "MagicNumber")
    fun fromUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}
