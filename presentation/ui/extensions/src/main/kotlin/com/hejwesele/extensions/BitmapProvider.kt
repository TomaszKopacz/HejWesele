package com.hejwesele.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BitmapProvider @Inject constructor(@ApplicationContext private val context: Context) {

    fun fromUrl(url: String): Bitmap =
        Glide.with(context).asBitmap().load(url).submit().get()

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
