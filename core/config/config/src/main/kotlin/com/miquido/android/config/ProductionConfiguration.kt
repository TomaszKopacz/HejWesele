package com.miquido.android.config

import com.miquido.android.config.Environment.PRODUCTION

object ProductionConfiguration : Configuration {
    override val environment = PRODUCTION
    override val baseHost = "https://template.com/api"
}
