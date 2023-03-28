package com.hejwesele.qr

import android.media.Image
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import javax.inject.Inject

class QrReader @Inject constructor() {
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    fun read(
        image: Image,
        rotation: Int,
        onRead: (String) -> Unit,
        onFail: () -> Unit
    ) {
        val input = InputImage.fromMediaImage(image, rotation)
        scanner.process(input)
            .addOnSuccessListener { barcodes ->
                if (barcodes.size > 0) {
                    onRead(barcodes[0].displayValue.orEmpty())
                }
            }
            .addOnFailureListener {
                onFail()
            }
    }
}
