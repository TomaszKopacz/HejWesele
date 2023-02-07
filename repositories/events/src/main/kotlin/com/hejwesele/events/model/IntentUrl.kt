package com.hejwesele.events.model

data class IntentUrl(
    val type: IntentType = IntentType.WWW,
    val intentPackage: String? = null,
    val url: String = ""
)
