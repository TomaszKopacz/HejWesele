package com.hejwesele.android.config

import com.hejwesele.android.config.Environment.SANDBOX

object SandboxConfiguration : Configuration {
    override val environment = SANDBOX
    override val baseHost = "https://sandbox.template.com/api"
}
