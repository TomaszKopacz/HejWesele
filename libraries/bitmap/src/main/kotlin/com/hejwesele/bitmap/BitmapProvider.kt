package com.hejwesele.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.hejwesele.android.osinfo.OsInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BitmapProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val osInfo: OsInfo
) {

    suspend fun fromUrl(url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val imageLoader = ImageLoader.Builder(context).build()
            val request = ImageRequest.Builder(context).data(url).build()
            val result = (imageLoader.execute(request) as SuccessResult).drawable

            (result as BitmapDrawable).bitmap
        }
    }

    @Suppress("DEPRECATION")
    fun fromUri(uri: Uri): Bitmap {
        return if (osInfo.isPOrHigher) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}
