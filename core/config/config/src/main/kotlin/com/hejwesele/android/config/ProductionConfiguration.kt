package com.hejwesele.android.config

import com.hejwesele.android.config.Environment.PRODUCTION

object ProductionConfiguration : Configuration {
    override val environment = PRODUCTION
    override val baseHost = "https://template.com/api"
}
