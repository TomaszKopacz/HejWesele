package com.miquido.androidtemplate.settings.theme

import androidx.lifecycle.viewModelScope
import com.miquido.android.mvvm.StateViewModel
import com.miquido.android.thememanager.Theme
import com.miquido.android.thememanager.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class ThemeBottomSheetUiState(
    val themes: List<Theme>
)

@HiltViewModel
internal class ThemeBottomSheetViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : StateViewModel<ThemeBottomSheetUiState>(ThemeBottomSheetUiState(themes = themeManager.getAvailableThemes())) {

    fun observeSelectedTheme(): Flow<Theme> = flow {
        themeManager.observeSelectedTheme().collect { emit(it) }
    }

    fun switchTheme(theme: Theme) = viewModelScope.launch {
        themeManager.switchTheme(theme)
    }
}
