package com.miquido.androidtemplate.settings.appinfo

import com.miquido.android.appinfo.AppInfo
import com.miquido.android.mvvm.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

internal data class AppInfoUiState(
    val appInformation: String
)

@HiltViewModel
internal class AppInfoViewModel @Inject constructor(appInfo: AppInfo) : StateViewModel<AppInfoUiState>(
    AppInfoUiState(appInformation = "${appInfo.appName} ${appInfo.versionName} (${appInfo.versionCode})")
)
