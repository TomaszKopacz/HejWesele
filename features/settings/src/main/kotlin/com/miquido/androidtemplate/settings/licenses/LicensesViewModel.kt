package com.miquido.androidtemplate.settings.licenses

import androidx.lifecycle.viewModelScope
import com.miquido.android.mvvm.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class LicensesUiState(
    val items: List<LicensesItem> = emptyList()
)

@HiltViewModel
internal class LicensesViewModel @Inject constructor(
    private val licensesUseCase: LicensesUseCase
) : StateViewModel<LicensesUiState>(LicensesUiState()) {

    init {
        viewModelScope.launch {
            val items = licensesUseCase.loadItems()
            updateState { copy(items = items) }
        }
    }
}
