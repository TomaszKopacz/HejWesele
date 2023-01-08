package com.miquido.androidtemplate.settings.licenses

import com.google.common.truth.Truth.assertThat
import com.miquido.android.licenses.Artifact
import com.miquido.android.licenses.FakeArtifactsRepository
import com.miquido.android.licenses.SpdxLicense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LicensesViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val artifactsRepository = FakeArtifactsRepository()
    private val licensesUseCase = LicensesUseCase(
        artifactsRepository = artifactsRepository,
        defaultDispatcher = dispatcher
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `test items loading`() = runTest(dispatcher) {
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

        val viewModel = LicensesViewModel(licensesUseCase)
        assertThat(viewModel.states.value.items).isEmpty()

        runCurrent()

        assertThat(viewModel.states.value.items).containsExactly(
            LicensesItem(
                title = "Example Library",
                linkText = "Apache License 2.0",
                linkUrl = "https://www.apache.org/licenses/LICENSE-2.0"
            )
        )
    }
}
