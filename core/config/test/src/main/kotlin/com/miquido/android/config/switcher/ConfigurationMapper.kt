package com.miquido.android.config.switcher

import com.miquido.android.config.Configuration
import com.miquido.android.config.Environment
import com.miquido.android.config.switcher.ConfigurationId.ENVIRONMENT
import com.miquido.android.config.switcher.ConfigurationItem.RadioGroup
import javax.inject.Inject

internal enum class ConfigurationId {
    ENVIRONMENT
}

internal data class RadioOption(
    val name: String,
    val checked: Boolean
)

internal sealed class ConfigurationItem {
    data class RadioGroup(
        val id: ConfigurationId,
        val name: String,
        val options: List<RadioOption>
    ) : ConfigurationItem()
}

internal class ConfigurationMapper @Inject constructor() {

    fun map(
        configurations: List<Configuration>,
        current: Environment
    ): List<ConfigurationItem> {
        return listOf(
            RadioGroup(
                id = ENVIRONMENT,
                name = "Environment",
                options = configurations.map {
                    RadioOption(name = it.environment.name, checked = it.environment == current)
                }
            )
        )
    }
}
