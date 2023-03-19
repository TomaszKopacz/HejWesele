package com.hejwesele.android.customtabs

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.pm.PackageManager.GET_RESOLVED_FILTER
import android.content.pm.PackageManager.ResolveInfoFlags
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.COLOR_SCHEME_SYSTEM
import androidx.browser.customtabs.CustomTabsIntent.ColorScheme
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_OFF
import androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
import com.hejwesele.customtabs.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

internal class AndroidXCustomTabs @Inject constructor(
    @ActivityContext private val context: Context
    // private val themeManager: ThemeManager
) : CustomTabs {

    companion object {
        private const val STABLE_PACKAGE = "com.android.chrome"
        private const val BETA_PACKAGE = "com.chrome.beta"
        private const val DEV_PACKAGE = "com.chrome.dev"
        private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"
    }

    override fun launch(url: String) {
        val packageName = getCustomTabsPackageName()
        if (packageName != null) {
            val intent = buildCustomTabsIntent(packageName)
            intent.launchUrl(context, Uri.parse(url))
        } else {
            Intent(ACTION_VIEW, Uri.parse(url)).let(context::startActivity)
        }
    }

    private fun getCustomTabsPackageName(): String? {
        val viewIntent = Intent()
            .setAction(ACTION_VIEW)
            .addCategory(CATEGORY_BROWSABLE)
            .setData(Uri.fromParts("http", "", null))

        val defaultViewHandlerPackage = getPackageName(viewIntent)
        val packagesSupportingCustomTabs = getResolveInfoList(viewIntent, 0)
            .mapNotNull { info ->
                val serviceIntent = Intent()
                    .setAction(ACTION_CUSTOM_TABS_CONNECTION)
                    .setPackage(info.activityInfo.packageName)

                if (getResolveInfo(serviceIntent) != null) info.activityInfo.packageName else null
            }

        return when {
            packagesSupportingCustomTabs.isEmpty() -> null
            packagesSupportingCustomTabs.size == 1 -> packagesSupportingCustomTabs.first()
            !defaultViewHandlerPackage.isNullOrEmpty() &&
                !hasSpecializedHandlerIntents(viewIntent) &&
                packagesSupportingCustomTabs.contains(defaultViewHandlerPackage) -> defaultViewHandlerPackage
            packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
            packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> BETA_PACKAGE
            packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> DEV_PACKAGE
            packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> LOCAL_PACKAGE
            else -> null
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun hasSpecializedHandlerIntents(intent: Intent): Boolean {
        return try {
            val handlers = getResolveInfoList(intent, GET_RESOLVED_FILTER)

            handlers.any { info ->
                info.filter != null &&
                    info.filter.countDataAuthorities() > 0 &&
                    info.filter.countDataPaths() > 0 &&
                    info.activityInfo != null
            }
        } catch (e: RuntimeException) {
            false
        }
    }

    private fun getPackageName(intent: Intent) = with(context.packageManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resolveActivity(intent, ResolveInfoFlags.of(0))?.activityInfo?.packageName
        } else {
            resolveActivity(intent, 0)?.activityInfo?.packageName
        }
    }

    private fun getResolveInfo(intent: Intent) = with(context.packageManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resolveService(intent, ResolveInfoFlags.of(0))
        } else {
            resolveService(intent, 0)
        }
    }

    private fun getResolveInfoList(intent: Intent, resolveInfoFlagValue: Int) = with(context.packageManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            queryIntentActivities(intent, ResolveInfoFlags.of(resolveInfoFlagValue.toLong()))
        } else {
            queryIntentActivities(intent, resolveInfoFlagValue)
        }
    }

    private fun buildCustomTabsIntent(packageName: String): CustomTabsIntent {
        return CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setColorScheme(getColorScheme())
            .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
            .setShareState(SHARE_STATE_OFF)
            .build()
            .apply { intent.`package` = packageName }
    }

    /*@ColorScheme
    private fun getColorScheme(): Int {
        return when (themeManager.getSelectedTheme()) {
            SYSTEM_DEFAULT -> COLOR_SCHEME_SYSTEM
            LIGHT -> COLOR_SCHEME_LIGHT
            DARK -> COLOR_SCHEME_DARK
        }
    }*/

    @ColorScheme
    private fun getColorScheme(): Int {
        return COLOR_SCHEME_SYSTEM
    }
}
