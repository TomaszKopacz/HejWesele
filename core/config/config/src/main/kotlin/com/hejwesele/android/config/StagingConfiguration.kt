package com.hejwesele.android.config

import com.hejwesele.android.config.Environment.STAGING

object StagingConfiguration : Configuration {
    override val environment = STAGING
    override val baseHost = "https://staging.template.com/api"
}
