package com.hejwesele.intent

fun String.mapIntentUrlModel(): IntentData {
    return when {
        startsWith(IntentUrlPrefix.INSTAGRAM) -> IntentData(
            intentType = IntentType.INSTAGRAM,
            intentPackage = IntentPackage.instagram,
            intentUrl = this
        )
        startsWith(IntentUrlPrefix.GOOGLE_MAPS) -> IntentData(
            intentType = IntentType.GOOGLE_MAPS,
            intentPackage = IntentPackage.google_maps,
            intentUrl = this
        )
        else -> {
            IntentData(
                intentType = IntentType.WWW,
                intentPackage = null,
                intentUrl = this
            )
        }
    }
}
