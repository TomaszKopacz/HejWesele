package com.miquido.android.appinfo

interface AppInfo {
    val appName: String
    val versionName: String
    val versionCode: Int
    val isDebug: Boolean
}
