package com.hejwesele.settings.appinfo

import com.hejwesele.android.appinfo.AppInfo
import com.hejwesele.android.mvvm.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

internal data class AppInfoUiState(
    val appInformation: String
)

@HiltViewModel
internal class AppInfoViewModel @Inject constructor(appInfo: AppInfo) : StateViewModel<AppInfoUiState>(
    AppInfoUiState(appInformation = "${appInfo.appName} ${appInfo.versionName} (${appInfo.versionCode})")
)
