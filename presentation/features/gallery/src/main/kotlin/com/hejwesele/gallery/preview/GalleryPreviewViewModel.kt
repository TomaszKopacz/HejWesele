package com.hejwesele.gallery.preview

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.gallery.destinations.GalleryPreviewDestination
import com.hejwesele.gallery.preview.usecase.PermissionHandler
import com.hejwesele.gallery.preview.usecase.SaveImage
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
    private val permissionHandler: PermissionHandler,
    private val savePhoto: SaveImage
) : StateViewModel<GalleryPreviewUiState>(GalleryPreviewUiState.DEFAULT) {

    private var state = State()

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }

            val photoUrls = GalleryPreviewDestination.argsFrom(state).photoUrls
            val selectedPhotoIndex = GalleryPreviewDestination.argsFrom(state).selectedPhotoIndex

            updateState {
                copy(
                    loading = false,
                    photoUrls = photoUrls,
                    selectedPhotoIndex = selectedPhotoIndex
                )
            }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            updateState { copy(closeScreen = triggered) }
        }
    }

    fun onSavePhotoClicked(photoUrl: String) {
        viewModelScope.launch {
            if (isAndroidQOrNewer) {
                savePhotoToStorage(photoUrl)
            } else {
                val permissions = arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                val permissionsGranted = permissionHandler.checkPermissions(permissions)

                if (permissionsGranted) {
                    savePhotoToStorage(photoUrl)
                } else {
                    state = state.copy(pendingPhotoUrl = photoUrl)
                    updateState {
                        copy(requestStoragePermissions = triggered(permissions))
                    }
                }
            }
        }
    }

    fun onStoragePermissionsRequested() {
        viewModelScope.launch {
            updateState { copy(requestStoragePermissions = consumed()) }
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
            updateState {
                copy(
                    showSavePhotoSuccess = consumed,
                    showSavePhotoError = consumed
                )
            }
        }
    }

    fun onScreenClosed() {
        viewModelScope.launch {
            updateState { copy(closeScreen = consumed) }
        }
    }

    private suspend fun savePhotoToStorage(photoUrl: String) {
        updateState { copy(savingPhoto = true) }
        viewModelScope.launch(Dispatchers.IO) {
            savePhoto(photoUrl)
                .onSuccess {
                    updateState {
                        copy(
                            showSavePhotoSuccess = triggered,
                            savingPhoto = false
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            showSavePhotoError = triggered,
                            savingPhoto = false
                        )
                    }
                }
        }
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    private val isAndroidQOrNewer: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private data class State(
        val pendingPhotoUrl: String? = null
    )
}

internal data class GalleryPreviewUiState(
    val closeScreen: StateEvent,
    val requestStoragePermissions: StateEventWithContent<Array<String>>,
    val showSavePhotoSuccess: StateEvent,
    val showSavePhotoError: StateEvent,
    val photoUrls: ArrayList<String>,
    val selectedPhotoIndex: Int,
    val loading: Boolean,
    val savingPhoto: Boolean
) {
    companion object {
        val DEFAULT = GalleryPreviewUiState(
            closeScreen = consumed,
            requestStoragePermissions = consumed(),
            showSavePhotoSuccess = consumed,
            showSavePhotoError = consumed,
            photoUrls = arrayListOf(),
            selectedPhotoIndex = 0,
            loading = false,
            savingPhoto = false
        )
    }
}
