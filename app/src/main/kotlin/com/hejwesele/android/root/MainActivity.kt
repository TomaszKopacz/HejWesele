package com.hejwesele.android.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.hejwesele.android.customtabs.CustomTabs
import com.hejwesele.android.navigation.Navigation
import com.hejwesele.android.splashscreen.Splash.initializeSplashScreen
import com.hejwesele.android.thememanager.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appRootProvider: AppRootProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { appRootProvider.Root() }
    }
}
