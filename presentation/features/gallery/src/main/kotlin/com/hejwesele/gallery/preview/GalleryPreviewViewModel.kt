package com.hejwesele.gallery.preview

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.osinfo.OsInfo
import com.hejwesele.gallery.destinations.GalleryPreviewDestination
import com.hejwesele.gallery.usecase.SaveImage
import com.hejwesele.permissions.PermissionsHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryPreviewViewModel @Inject constructor(
    state: SavedStateHandle,
    private val permissionsHandler: PermissionsHandler,
    private val osInfo: OsInfo,
    private val savePhoto: SaveImage
) : StateEventsViewModel<GalleryPreviewUiState, GalleryPreviewUiEvents>(
    GalleryPreviewUiState.Default,
    GalleryPreviewUiEvents.Default
) {

    private var state = State()

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val photoUrls = GalleryPreviewDestination.argsFrom(state).photoUrls
            val selectedPhotoIndex = GalleryPreviewDestination.argsFrom(state).selectedPhotoIndex

            updateState {
                copy(
                    isLoading = false,
                    photoUrls = photoUrls,
                    selectedPhotoIndex = selectedPhotoIndex
                )
            }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            updateEvents { copy(closeScreen = triggered) }
        }
    }

    fun onSavePhotoClicked(photoUrl: String) {
        viewModelScope.launch {
            if (osInfo.isQOrHigher) {
                savePhotoToStorage(photoUrl)
            } else {
                val permissions = arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                val permissionsGranted = permissionsHandler.checkPermissions(permissions)

                if (permissionsGranted) {
                    savePhotoToStorage(photoUrl)
                } else {
                    state = state.copy(pendingPhotoUrl = photoUrl)
                    updateEvents { copy(requestStoragePermissions = triggered(permissions)) }
                }
            }
        }
    }

    fun onStoragePermissionsRequested() {
        viewModelScope.launch {
            updateEvents { copy(requestStoragePermissions = consumed()) }
        }
    }

    fun onStoragePermissionsResult(result: Map<String, Boolean>) {
        viewModelScope.launch {
            val permissionsGranted = result.values.reduce { acc, next -> acc && next }
            if (permissionsGranted) {
                state.pendingPhotoUrl?.let { savePhotoToStorage(it) }
                state = state.copy(pendingPhotoUrl = null)
            }
        }
    }

    fun onSavePhotoResultShown() {
        viewModelScope.launch {
            updateEvents { copy(showSavePhotoSuccess = consumed, showSavePhotoError = consumed) }
        }
    }

    fun onScreenClosed() {
        viewModelScope.launch {
            updateEvents { copy(closeScreen = consumed) }
        }
    }

    private suspend fun savePhotoToStorage(photoUrl: String) {
        updateState { copy(isSavingPhoto = true) }
        viewModelScope.launch(Dispatchers.IO) {
            savePhoto(photoUrl)
                .onSuccess {
                    updateState { copy(isSavingPhoto = false) }
                    updateEvents { copy(showSavePhotoSuccess = triggered) }
                }
                .onFailure {
                    updateState { copy(isSavingPhoto = false) }
                    updateEvents { copy(showSavePhotoError = triggered) }
                }
        }
    }

    private data class State(
        val pendingPhotoUrl: String? = null
    )
}

internal data class GalleryPreviewUiState(
    val isLoading: Boolean,
    val photoUrls: ArrayList<String>,
    val selectedPhotoIndex: Int,
    val isSavingPhoto: Boolean
) {
    companion object {
        val Default = GalleryPreviewUiState(
            isLoading = false,
            photoUrls = arrayListOf(),
            selectedPhotoIndex = 0,
            isSavingPhoto = false
        )
    }
}

internal data class GalleryPreviewUiEvents(
    val closeScreen: StateEvent,
    val requestStoragePermissions: StateEventWithContent<Array<String>>,
    val showSavePhotoSuccess: StateEvent,
    val showSavePhotoError: StateEvent
) {
    companion object {
        val Default = GalleryPreviewUiEvents(
            closeScreen = consumed,
            requestStoragePermissions = consumed(),
            showSavePhotoSuccess = consumed,
            showSavePhotoError = consumed
        )
    }
}
