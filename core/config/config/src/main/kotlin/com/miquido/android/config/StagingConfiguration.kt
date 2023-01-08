package com.miquido.android.config

import com.miquido.android.config.Environment.STAGING

object StagingConfiguration : Configuration {
    override val environment = STAGING
    override val baseHost = "https://staging.template.com/api"
}
