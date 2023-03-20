package com.hejwesele

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.hejwesele.android.analytics.Analytics
import com.hejwesele.android.crashlytics.Crashlytics
import com.hejwesele.android.thememanager.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.migration.CustomInject
import dagger.hilt.android.migration.CustomInjection
import logcat.AndroidLogcatLogger
import logcat.LogPriority.VERBOSE
import javax.inject.Inject

@CustomInject
@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    internal lateinit var crashlytics: Crashlytics

    @Inject
    internal lateinit var analytics: Analytics

    @Inject
    internal lateinit var themeManager: ThemeManager

    @Inject
    internal lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        CustomInjection.inject(this)

        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)

        analytics.enable()
        crashlytics.enable()

        themeManager.applyTheme()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
