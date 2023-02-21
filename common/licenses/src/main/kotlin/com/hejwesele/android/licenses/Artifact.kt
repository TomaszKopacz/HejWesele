package com.hejwesele.android.licenses

import java.util.Collections.emptyList

data class Artifact(
    val groupId: String,
    val artifactId: String,
    val name: String? = null,
    val spdxLicenses: List<SpdxLicense> = emptyList(),
    val unknownLicenses: List<UnknownLicense> = emptyList()
)

data class SpdxLicense(
    val name: String,
    val url: String
)

data class UnknownLicense(
    val name: String? = null,
    val url: String
)
