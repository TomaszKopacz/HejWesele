package com.hejwesele.android.appinfo

import android.content.Context
import com.hejwesele.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClientNameImpl @Inject constructor(
    @ApplicationContext context: Context,
    appInfo: AppInfo,
    systemInfo: SystemInfo
) : ClientName {

    override val name = context.getString(
        R.string.client_name_format,
        appInfo.appName,
        appInfo.versionName,
        systemInfo.systemName,
        systemInfo.systemVersion
    )
}
