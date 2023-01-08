package com.miquido.android.config

import com.miquido.android.config.Environment.SANDBOX

object SandboxConfiguration : Configuration {
    override val environment = SANDBOX
    override val baseHost = "https://sandbox.template.com/api"
}
