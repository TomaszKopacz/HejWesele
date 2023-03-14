package com.hejwesele.gallery.preview.usecase

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun checkPermissions(permissions: Array<String>) =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
}
