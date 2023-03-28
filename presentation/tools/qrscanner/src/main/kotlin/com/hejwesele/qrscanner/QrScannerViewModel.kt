package com.hejwesele.qrscanner

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class QrScannerViewModel @Inject constructor(
    val qrCodeAnalyser: QrCodeAnalyser
) : ViewModel()
