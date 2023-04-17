package com.hejwesele.imageloader

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.Coil
import coil.compose.AsyncImagePainter.State.Empty
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.AsyncImagePainter.State.Success
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.ImageLoader as CoilImageLoader

object ImageLoader {

    private const val MEMORY_CACHE_MAX_SIZE = 0.25 // 25%
    private const val DISK_CACHE_MAX_SIZE = 64L * 1024 * 1024 // 64MB
    private const val DISK_CACHE_DIRECTORY_NAME = "image_cache"

    fun install(context: Context) {
        Coil.setImageLoader {
            getCoilImageLoader(context)
        }
    }

    fun getImageRequest(
        context: Context,
        url: String
    ): ImageRequest {
        return ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(url)
            .diskCacheKey(url)
            .build()
    }

    private fun getCoilImageLoader(context: Context): CoilImageLoader = CoilImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(MEMORY_CACHE_MAX_SIZE)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve(DISK_CACHE_DIRECTORY_NAME))
                .maxSizeBytes(DISK_CACHE_MAX_SIZE)
                .build()
        }
        .respectCacheHeaders(false)
        .build()
}

@Composable
fun CachedImage(
    modifier: Modifier = Modifier,
    url: String,
    loader: @Composable () -> Unit,
    fallback: @Composable () -> Unit,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageLoader.getImageRequest(
            context = context,
            url = url
        ),
        contentDescription = null,
        contentScale = contentScale
    ) {
        when (painter.state) {
            is Loading -> loader()
            is Error, Empty -> fallback()
            is Success -> {
                SubcomposeAsyncImageContent()

                val imageSource = (painter.state as Success).result.dataSource
                Log.d("TOMASZKOPACZ", "Image $url successfully loaded from source: $imageSource")
            }
        }
    }
}
