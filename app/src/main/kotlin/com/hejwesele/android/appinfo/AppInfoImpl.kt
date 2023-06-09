package com.hejwesele.android.appinfo

import android.content.Context
import com.hejwesele.BuildConfig
import com.hejwesele.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppInfoImpl @Inject constructor(@ApplicationContext context: Context) : AppInfo {
    override val appName = context.getString(R.string.app_name)
    override val versionName = BuildConfig.VERSION_NAME
    override val versionCode = BuildConfig.VERSION_CODE
    override val isDebug = BuildConfig.DEBUG
}
