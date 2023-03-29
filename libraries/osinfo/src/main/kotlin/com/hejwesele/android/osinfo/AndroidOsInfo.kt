package com.hejwesele.android.osinfo

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import javax.inject.Inject

internal class AndroidOsInfo @Inject constructor() : OsInfo {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    override val isQOrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    override val isPOrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}
