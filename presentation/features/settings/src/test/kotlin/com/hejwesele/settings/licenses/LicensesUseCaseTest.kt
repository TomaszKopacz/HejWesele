package com.hejwesele.settings.licenses

import com.google.common.truth.Truth.assertThat
import com.hejwesele.android.licenses.Artifact
import com.hejwesele.android.licenses.SpdxLicense
import com.hejwesele.android.licenses.UnknownLicense
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class LicensesUseCaseTest {

    private val dispatcher = StandardTestDispatcher()

    private val artifactsRepository = FakeArtifactsRepository()
    private val licensesUseCase = LicensesUseCase(
        artifactsRepository = artifactsRepository,
        defaultDispatcher = dispatcher
    )

    @Test
    fun `test simple artifact`() = runTest(dispatcher) {
        artifactsRepository.artifacts = listOf(
            Artifact(
                groupId = "org.example",
                artifactId = "library",
                name = "Example Library",
                spdxLicenses = listOf(
                    SpdxLicense(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    )
                )
            )
        )
        assertThat(licensesUseCase.loadItems()).containsExactly(
            LicensesItem(
                title = "Example Library",
                linkText = "Apache License 2.0",
                linkUrl = "https://www.apache.org/licenses/LICENSE-2.0"
            )
        )
    }

    @Test
    fun `test artifact with no name`() = runTest(dispatcher) {
        artifactsRepository.artifacts = listOf(
            Artifact(
                groupId = "org.example",
                artifactId = "library",
                spdxLicenses = listOf(
                    SpdxLicense(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    )
                )
            )
        )
        assertThat(licensesUseCase.loadItems()).containsExactly(
            LicensesItem(
                title = "org.example:library",
                linkText = "Apache License 2.0",
                linkUrl = "https://www.apache.org/licenses/LICENSE-2.0"
            )
        )
    }

    @Test
    fun `test artifact with unknown license`() = runTest(dispatcher) {
        artifactsRepository.artifacts = listOf(
            Artifact(
                groupId = "org.example",
                artifactId = "library",
                name = "Example Library",
                unknownLicenses = listOf(
                    UnknownLicense(
                        name = "Some unknown license",
                        url = "https://www.unknown.org/license"
                    )
                )
            )
        )
        assertThat(licensesUseCase.loadItems()).containsExactly(
            LicensesItem(
                title = "Example Library",
                linkText = "Some unknown license",
                linkUrl = "https://www.unknown.org/license"
            )
        )
    }

    @Test
    fun `test artifact with unknown license with no name`() = runTest(dispatcher) {
        artifactsRepository.artifacts = listOf(
            Artifact(
                groupId = "org.example",
                artifactId = "library",
                name = "Example Library",
                unknownLicenses = listOf(
                    UnknownLicense(
                        url = "https://www.unknown.org/license"
                    )
                )
            )
        )
        assertThat(licensesUseCase.loadItems()).containsExactly(
            LicensesItem(
                title = "Example Library",
                linkText = "https://www.unknown.org/license",
                linkUrl = "https://www.unknown.org/license"
            )
        )
    }

    @Test
    fun `test duplicated artifacts`() = runTest(dispatcher) {
        val artifact = Artifact(
            groupId = "org.example",
            artifactId = "library",
            name = "Example Library",
            spdxLicenses = listOf(
                SpdxLicense(
                    name = "Apache License 2.0",
                    url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
            )
        )

        artifactsRepository.artifacts = listOf(artifact, artifact)

        assertThat(licensesUseCase.loadItems()).containsExactly(
            LicensesItem(
                title = "Example Library",
                linkText = "Apache License 2.0",
                linkUrl = "https://www.apache.org/licenses/LICENSE-2.0"
            )
        )
    }

    @Test
    fun `test artifacts sorting`() = runTest(dispatcher) {
        val artifact1 = Artifact(
            groupId = "org.example",
            artifactId = "library",
            name = "Z Example Library",
            spdxLicenses = listOf(
                SpdxLicense(
                    name = "Apache License 2.0",
                    url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
            )
        )
        val artifact2 = Artifact(
            groupId = "org.example",
            artifactId = "library",
            name = "A Example Library",
            spdxLicenses = listOf(
                SpdxLicense(
                    name = "Apache License 2.0",
                    url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
            )
        )

        artifactsRepository.artifacts = listOf(artifact1, artifact2)

        assertThat(licensesUseCase.loadItems()).containsExactly(
            LicensesItem(
                title = "A Example Library",
                linkText = "Apache License 2.0",
                linkUrl = "https://www.apache.org/licenses/LICENSE-2.0"
            ),
            LicensesItem(
                title = "Z Example Library",
                linkText = "Apache License 2.0",
                linkUrl = "https://www.apache.org/licenses/LICENSE-2.0"
            )
        ).inOrder()
    }
}
