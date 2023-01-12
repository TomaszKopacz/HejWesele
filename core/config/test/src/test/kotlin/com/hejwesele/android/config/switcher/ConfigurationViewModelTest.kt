package com.hejwesele.android.config.switcher

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.hejwesele.android.config.ConfigurationRepository
import com.hejwesele.android.config.Environment
import com.hejwesele.android.config.ProductionConfiguration
import com.hejwesele.android.config.SandboxConfiguration
import com.hejwesele.android.config.StagingConfiguration
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConfigurationViewModelTest {

    private val mockConfigurationRepository: ConfigurationRepository = mock {
        on { getAll() } doReturn listOf(
            SandboxConfiguration,
            StagingConfiguration,
            ProductionConfiguration
        )
        onBlocking { getCurrentEnvironment() } doReturn Environment.SANDBOX
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `display loaded state with fetched environments after initialization`() = runTest {
        val viewModel = ConfigurationViewModel(
            repository = mockConfigurationRepository,
            mapper = ConfigurationMapper()
        )
        advanceUntilIdle()

        val expectedItem = ConfigurationItem.RadioGroup(
            id = ConfigurationId.ENVIRONMENT,
            name = "Environment",
            options = listOf(
                RadioOption(name = "SANDBOX", checked = true),
                RadioOption(name = "STAGING", checked = false),
                RadioOption(name = "PRODUCTION", checked = false),
            )
        )
        assertThat(viewModel.states.value).isEqualTo(ConfigurationUiState.Loaded(items = listOf(expectedItem)))
    }

    @Test
    fun `select clicked radio button when radio is checked`() = runTest {
        val viewModel = ConfigurationViewModel(
            repository = mockConfigurationRepository,
            mapper = ConfigurationMapper()
        )
        advanceUntilIdle()

        viewModel.onRadioChecked(id = ConfigurationId.ENVIRONMENT, name = "STAGING")

        val expectedItem = ConfigurationItem.RadioGroup(
            id = ConfigurationId.ENVIRONMENT,
            name = "Environment",
            options = listOf(
                RadioOption(name = "SANDBOX", checked = false),
                RadioOption(name = "STAGING", checked = true),
                RadioOption(name = "PRODUCTION", checked = false),
            )
        )
        assertThat(viewModel.states.value).isEqualTo(ConfigurationUiState.Loaded(items = listOf(expectedItem)))
    }

    @Test
    fun `set selected environment and close the screen when save is clicked`() = runTest {
        val viewModel = ConfigurationViewModel(
            repository = mockConfigurationRepository,
            mapper = ConfigurationMapper()
        )
        advanceUntilIdle()

        viewModel.actions.test {
            viewModel.onRadioChecked(id = ConfigurationId.ENVIRONMENT, name = "STAGING")
            viewModel.onSaveClicked()
            advanceUntilIdle()

            verify(mockConfigurationRepository).setCurrentEnvironment(Environment.STAGING)
            assertThat(awaitItem()).isEqualTo(ConfigurationUiAction.Close)
        }
    }
}
