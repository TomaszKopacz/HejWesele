package com.hejwesele.android.tools

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.hejwesele.android.theme.md_theme_dark_background
import com.hejwesele.android.theme.md_theme_dark_onBackground

object ImageCropper {
    private const val IMAGE_DIRECTORY = "image/*"

    private lateinit var imagePickerLauncher: ManagedActivityResultLauncher<String, Uri?>
    private lateinit var imageCropperLauncher: ManagedActivityResultLauncher<CropImageContractOptions, CropImageView.CropResult>

    private var onImageCropped: ((Uri) -> Unit)? = null
    private var onImageCropError: (() -> Unit)? = null

    @SuppressLint("ComposableNaming")
    @Composable
    fun install() {
        val cropImageOptions = CropImageOptions(
            cropShape = CropImageView.CropShape.RECTANGLE,
            fixAspectRatio = true,
            aspectRatioX = 1,
            aspectRatioY = 1,
            toolbarColor = md_theme_dark_background.toArgb(),
            toolbarBackButtonColor = md_theme_dark_onBackground.toArgb(),
            activityBackgroundColor = md_theme_dark_background.toArgb(),
            activityMenuIconColor = md_theme_dark_onBackground.toArgb(),
            activityMenuTextColor = md_theme_dark_onBackground.toArgb(),
            guidelinesColor = Color.Transparent.toArgb(),
            borderLineColor = Color.Transparent.toArgb(),
            progressBarColor = MaterialTheme.colorScheme.secondaryContainer.toArgb(),
            cropMenuCropButtonTitle = "OK",
        )

        imageCropperLauncher = rememberLauncherForActivityResult(CropImageContract()) { cropResult ->
            val uri = cropResult.uriContent
            if (cropResult.isSuccessful && uri != null) {
                onImageCropped?.invoke(uri)
            } else {
                onImageCropError?.invoke()
            }
        }

        imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val contract = CropImageContractOptions(uri, cropImageOptions)
                imageCropperLauncher.launch(contract)
            }
        }
    }

    fun launch(
       onImageCropped: ((Uri) -> Unit)? = null,
       onImageCropError: (() -> Unit)? = null
    ) {
        ImageCropper.onImageCropped = onImageCropped
        ImageCropper.onImageCropError = onImageCropError

        imagePickerLauncher.launch(IMAGE_DIRECTORY)
    }
}
