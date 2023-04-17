package com.hejwesele.qrscanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.hejwesele.qr.QrReader
import javax.inject.Inject

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
internal class QrCodeAnalyser @Inject constructor(
    private val qrReader: QrReader
) : ImageAnalysis.Analyzer {

    private var onSuccess: ((String) -> Unit)? = null
    private var onFailure: (() -> Unit)? = null

    override fun analyze(imageProxy: ImageProxy) {
        val image = imageProxy.image
        val rotation = imageProxy.imageInfo.rotationDegrees

        if (image != null) {
            qrReader.read(
                image = image,
                rotation = rotation,
                onRead = { text -> onSuccess?.invoke(text) },
                onFail = { onFailure?.invoke() },
                onComplete = { imageProxy.close() }
            )
        }

        imageProxy.close()
    }

    fun setOnSuccessListener(action: (String) -> Unit): QrCodeAnalyser {
        onSuccess = action
        return this
    }

    fun setOnFailureListener(action: () -> Unit): QrCodeAnalyser {
        onFailure = action
        return this
    }
}
