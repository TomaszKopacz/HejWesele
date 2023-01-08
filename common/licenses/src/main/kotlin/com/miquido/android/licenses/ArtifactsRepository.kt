package com.miquido.android.licenses

interface ArtifactsRepository {
    suspend fun loadArtifacts(): List<Artifact>
}
