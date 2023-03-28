package com.hejwesele.qrscanner

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ScaleType.FILL_CENTER
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.hejwesele.android.theme.Dimension
import java.lang.Double.min
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun QrScannerView(
    modifier: Modifier = Modifier,
    onScanned: (String) -> Unit
) {
    QrScannerEntryPoint(
        modifier = modifier,
        onScanned = onScanned
    )
}

@Composable
private fun QrScannerEntryPoint(
    modifier: Modifier = Modifier,
    viewModel: QrScannerViewModel = hiltViewModel(),
    onScanned: (String) -> Unit
) {
    val threadExecutor = Executors.newSingleThreadExecutor()
    val qrCodeAnalyser = viewModel.qrCodeAnalyser

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context).also { it.scaleType = FILL_CENTER }
                val cameraProvider = ProcessCameraProvider.getInstance(context)

                qrCodeAnalyser.setOnSuccessListener { text ->
                    onScanned(text)
                }

                cameraProvider.setQrListener(
                    context = context,
                    threadExecutor = threadExecutor,
                    surfaceProvider = previewView.surfaceProvider,
                    qrCodeAnalyser = qrCodeAnalyser
                )

                previewView
            }
        )
        SquareWindowOverlay()
    }
}

@Composable
private fun SquareWindowOverlay() {
    val outlineColor = MaterialTheme.colorScheme.outline

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val margin = Dimension.marginLarge.toPx()
        val cornerRadius = Dimension.radiusRoundedCornerLarge.toPx()
        val lowerDimension = min(canvasWidth.toDouble(), canvasHeight.toDouble()).toFloat()
        val windowSize = lowerDimension - 2 * margin
        val left = (canvasWidth - windowSize) / 2
        val right = left + windowSize
        val top = (canvasHeight - windowSize) / 2
        val bottom = top + windowSize

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        left = left,
                        top = top,
                        right = right,
                        bottom = bottom
                    ),
                    topLeft = CornerRadius(cornerRadius, cornerRadius),
                    topRight = CornerRadius(cornerRadius, cornerRadius),
                    bottomRight = CornerRadius(cornerRadius, cornerRadius),
                    bottomLeft = CornerRadius(cornerRadius, cornerRadius)
                )
            )
        }

        val outlineWidth = Dimension.borderWidthNormal
        val outlineCornerSize = 2 * margin

        clipPath(
            path = path,
            clipOp = ClipOp.Difference
        ) {
            drawRect(Color.Black.copy(alpha = 0.5f))
        }

        drawArc(
            topLeft = Offset(left, top),
            size = Size(outlineCornerSize, outlineCornerSize),
            startAngle = 180.0f,
            sweepAngle = 90.0f,
            useCenter = false,
            style = Stroke(width = outlineWidth.toPx()),
            color = outlineColor
        )
        drawArc(
            topLeft = Offset(right - outlineCornerSize, top),
            size = Size(outlineCornerSize, outlineCornerSize),
            startAngle = 270.0f,
            sweepAngle = 90.0f,
            useCenter = false,
            style = Stroke(width = outlineWidth.toPx()),
            color = outlineColor
        )
        drawArc(
            topLeft = Offset(right - outlineCornerSize, bottom - outlineCornerSize),
            size = Size(outlineCornerSize, outlineCornerSize),
            startAngle = 0.0f,
            sweepAngle = 90.0f,
            useCenter = false,
            style = Stroke(width = outlineWidth.toPx()),
            color = outlineColor
        )
        drawArc(
            topLeft = Offset(left, bottom - outlineCornerSize),
            size = Size(outlineCornerSize, outlineCornerSize),
            startAngle = 90.0f,
            sweepAngle = 90.0f,
            useCenter = false,
            style = Stroke(width = outlineWidth.toPx()),
            color = outlineColor
        )
    }
}

private fun ListenableFuture<ProcessCameraProvider>.setQrListener(
    context: Context,
    threadExecutor: Executor,
    surfaceProvider: SurfaceProvider,
    qrCodeAnalyser: QrCodeAnalyser
) {
    addListener(
        {
            val cameraProvider = get()
            val imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(surfaceProvider) }
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also { it.setAnalyzer(threadExecutor, qrCodeAnalyser) }

            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                context as ComponentActivity,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )
        },
        ContextCompat.getMainExecutor(context)
    )
}
