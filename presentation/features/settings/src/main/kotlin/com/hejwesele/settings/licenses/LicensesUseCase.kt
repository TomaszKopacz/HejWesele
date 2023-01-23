package com.hejwesele.settings.licenses

import com.hejwesele.android.coroutines.di.DefaultDispatcher
import com.hejwesele.android.licenses.Artifact
import com.hejwesele.android.licenses.ArtifactsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LicensesUseCase @Inject constructor(
    private val artifactsRepository: ArtifactsRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend fun loadItems(): List<LicensesItem> {
        val artifacts = artifactsRepository.loadArtifacts()
        return artifacts.mapToItems()
    }

    private suspend fun List<Artifact>.mapToItems(): List<LicensesItem> {
        return withContext(defaultDispatcher) {
            val items = map {
                val name = it.name ?: "${it.groupId}:${it.artifactId}"
                val spdxLicense = it.spdxLicenses.firstOrNull()
                val unknownLicense = it.unknownLicenses.firstOrNull()
                val licenseName = requireNotNull(spdxLicense?.name ?: unknownLicense?.name ?: unknownLicense?.url)
                val licenseUrl = requireNotNull(spdxLicense?.url ?: unknownLicense?.url)

                LicensesItem(
                    title = name,
                    linkText = licenseName,
                    linkUrl = licenseUrl
                )
            }

            items.toSet().sortedBy { it.title.lowercase() }
        }
    }
}
