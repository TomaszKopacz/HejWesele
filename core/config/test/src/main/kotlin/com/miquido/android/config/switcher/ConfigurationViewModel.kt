package com.miquido.android.config.switcher

import androidx.lifecycle.viewModelScope
import com.miquido.android.config.ConfigurationRepository
import com.miquido.android.config.Environment
import com.miquido.android.config.switcher.ConfigurationId.ENVIRONMENT
import com.miquido.android.config.switcher.ConfigurationUiAction.Close
import com.miquido.android.config.switcher.ConfigurationUiState.Loaded
import com.miquido.android.config.switcher.ConfigurationUiState.Loading
import com.miquido.android.mvvm.StateActionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed class ConfigurationUiState {
    internal object Loading : ConfigurationUiState()

    internal data class Loaded(
        val items: List<ConfigurationItem>
    ) : ConfigurationUiState()
}

internal sealed class ConfigurationUiAction {
    object Close : ConfigurationUiAction()
}

@HiltViewModel
internal class ConfigurationViewModel @Inject constructor(
    private val repository: ConfigurationRepository,
    private val mapper: ConfigurationMapper
) : StateActionsViewModel<ConfigurationUiState, ConfigurationUiAction>(Loading) {

    private val configurations by lazy { repository.getAll() }
    private var currentEnvironment: Environment? = null

    init {
        viewModelScope.launch {
            val current = repository.getCurrentEnvironment()
            currentEnvironment = current
            setState(Loaded(mapper.map(configurations, current)))
        }
    }

    fun onRadioChecked(id: ConfigurationId, name: String) {
        when (id) {
            ENVIRONMENT -> {
                val checked = enumValueOf<Environment>(name)
                currentEnvironment = checked
                setState(Loaded(mapper.map(configurations, checked)))
            }
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            currentEnvironment?.let {
                repository.setCurrentEnvironment(it)
            }
            emitAction(Close)
        }
    }
}
