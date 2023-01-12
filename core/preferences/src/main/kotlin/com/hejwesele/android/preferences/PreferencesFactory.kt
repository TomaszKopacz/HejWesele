package com.hejwesele.android.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesFactory @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun create(name: String): Preferences {
        return DataStorePreferences(name, context)
    }
}
