package com.hejwesele.gallery.model

import com.canhub.cropper.CropImageContractOptions

sealed class GalleryUiAction {
    class OpenDeviceGallery(val directory: String) : GalleryUiAction()
    class OpenImageCropper(val options: CropImageContractOptions) : GalleryUiAction()
}
