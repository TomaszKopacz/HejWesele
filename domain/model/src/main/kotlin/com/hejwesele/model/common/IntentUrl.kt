package com.hejwesele.model.common

data class IntentUrl(
    val type: IntentType,
    val intentPackage: String?,
    val url: String
)

enum class IntentType {
    INSTAGRAM,
    GOOGLE_MAPS,
    WWW
}

object IntentPackage {
    const val instagram = "com.instagram.android"
    const val google_maps = "com.google.android.apps.maps"
}

object UrlPrefix {
    const val INSTAGRAM = "https://www.instagram.com/"
    const val GOOGLE_MAPS = "https://goo.gl/maps/"
}
