package com.hejwesele.android.licenses

interface ArtifactsRepository {
    suspend fun loadArtifacts(): List<Artifact>
}
