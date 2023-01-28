package com.hejwesele.home

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.home.model.HomeTileUiModel
import com.hejwesele.model.home.HomeTile
import com.hejwesele.model.onError
import com.hejwesele.model.onSuccess
import com.hejwesele.usecase.home.GetHomeTiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getHomeTiles: GetHomeTiles
) : StateViewModel<HomeUiState>(HomeUiState.DEFAULT) {

    fun init() {
        viewModelScope.launch {
            getHomeTiles("7AtxeYEUkKYB5M6cUK2g")
                .onSuccess { homeTiles ->
                    updateState {
                        copy(
                            isLoading = false,
                            tiles = homeTiles.map { it.toUiModel() }
                        )
                    }
                }
                .onError { _ ->
                    updateState {
                        copy(
                            isLoading = false,
                            tiles = emptyList()
                        )
                    }
                }
        }
    }

    private fun HomeTile.toUiModel() = HomeTileUiModel(
        type = type,
        title = title,
        subtitle = subtitle,
        description = description,
        photoUrls = photoUrls
    )
}
