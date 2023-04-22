package com.hejwesele.information.usecase

import com.hejwesele.android.appinfo.AppInfo
import javax.inject.Inject

class GetAppVersion @Inject constructor(private val appInfo: AppInfo) {

    operator fun invoke() = "${appInfo.versionName} (${appInfo.versionCode})"
}
