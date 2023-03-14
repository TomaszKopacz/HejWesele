package com.hejwesele.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Suppress("SwallowedException")
fun openActivity(context: Context, activityPackage: String?, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        intent.setPackage(activityPackage)
        ContextCompat.startActivity(context, intent, null)
    } catch (exception: ActivityNotFoundException) {
        intent.setPackage(null)
        ContextCompat.startActivity(context, intent, null)
    }
}