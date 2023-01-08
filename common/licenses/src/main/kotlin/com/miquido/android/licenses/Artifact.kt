package com.miquido.android.licenses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artifact(
    val groupId: String,
    val artifactId: String,
    val name: String? = null,
    val spdxLicenses: List<SpdxLicense> = emptyList(),
    val unknownLicenses: List<UnknownLicense> = emptyList()
)

@JsonClass(generateAdapter = true)
data class SpdxLicense(
    val name: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class UnknownLicense(
    val name: String? = null,
    val url: String
)
