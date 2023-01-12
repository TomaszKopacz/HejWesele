package com.hejwesele.android.config

enum class Environment {
    SANDBOX, STAGING, PRODUCTION
}

interface Configuration {
    val environment: Environment
    val baseHost: String
}
