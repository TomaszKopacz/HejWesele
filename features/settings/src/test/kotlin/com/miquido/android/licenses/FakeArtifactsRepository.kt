package com.miquido.android.licenses

class FakeArtifactsRepository : ArtifactsRepository {

    lateinit var artifacts: List<Artifact>

    override suspend fun loadArtifacts(): List<Artifact> {
        return artifacts
    }
}
