package com.hejwesele.settings.theme

import androidx.lifecycle.ViewModel
import com.hejwesele.android.thememanager.Theme
import com.hejwesele.android.thememanager.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
internal class ThemeInfoViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    fun observeSelectedTheme(): Flow<Theme> = flow {
        themeManager.observeSelectedTheme().collect { emit(it) }
    }
}
