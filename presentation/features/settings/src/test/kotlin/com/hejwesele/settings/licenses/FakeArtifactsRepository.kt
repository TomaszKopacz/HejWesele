package com.hejwesele.settings.licenses

import com.hejwesele.android.licenses.Artifact
import com.hejwesele.android.licenses.ArtifactsRepository

internal class FakeArtifactsRepository : ArtifactsRepository {

    lateinit var artifacts: List<Artifact>

    override suspend fun loadArtifacts(): List<Artifact> {
        return artifacts
    }
}
