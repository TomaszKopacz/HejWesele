package com.miquido.android.config

enum class Environment {
    SANDBOX, STAGING, PRODUCTION
}

interface Configuration {
    val environment: Environment
    val baseHost: String
}
