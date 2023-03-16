package com.hejwesele.intent

data class IntentData(
    val intentType: IntentType = IntentType.WWW,
    val intentPackage: String? = null,
    val intentUrl: String
)
