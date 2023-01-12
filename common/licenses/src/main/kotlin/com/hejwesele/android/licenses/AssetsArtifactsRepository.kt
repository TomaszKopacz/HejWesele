package com.hejwesele.android.licenses

import android.content.Context
import com.hejwesele.android.coroutines.di.DefaultDispatcher
import com.hejwesele.android.coroutines.di.IoDispatcher
import com.hejwesele.android.json.Json
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AssetsArtifactsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ArtifactsRepository {

    companion object {
        private const val ARTIFACTS_FILENAME = "licenses/artifacts.json"
    }

    private val assetsManager by lazy { context.assets }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun loadArtifacts(): List<Artifact> {
        val content = withContext(ioDispatcher) {
            assetsManager
                .open(ARTIFACTS_FILENAME)
                .bufferedReader()
                .use { it.readText() }
        }
        return withContext(defaultDispatcher) {
            json.fromJson(content, Types.newParameterizedType(List::class.java, Artifact::class.java))!!
        }
    }
}
