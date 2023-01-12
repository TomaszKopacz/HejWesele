package com.hejwesele.android.appinfo

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import com.hejwesele.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemInfoImpl @Inject constructor(@ApplicationContext context: Context) : SystemInfo {
    override val systemName = context.getString(R.string.system_name)
    override val systemVersion = context.getString(R.string.system_version_format, SDK_INT)
}
