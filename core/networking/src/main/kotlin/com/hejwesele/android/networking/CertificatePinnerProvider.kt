package com.hejwesele.android.networking

import okhttp3.CertificatePinner

internal object CertificatePinnerProvider {

    // private const val HOSTNAME = "TODO"

    fun getCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            // .add(HOSTNAME, "sha256/...")
            .build()
    }
}
