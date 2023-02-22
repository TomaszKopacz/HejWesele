package com.hejwesele.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.flowWithLifecycle
import com.hejwesele.android.customtabs.CustomTabs
import com.hejwesele.android.customtabs.LocalCustomTabs
import com.hejwesele.android.navigation.Navigation
import com.hejwesele.android.splashscreen.Splash.initializeSplashScreen
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.thememanager.Theme
import com.hejwesele.android.thememanager.Theme.DARK
import com.hejwesele.android.thememanager.Theme.LIGHT
import com.hejwesele.android.thememanager.Theme.SYSTEM_DEFAULT
import com.hejwesele.android.thememanager.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var navigation: Navigation

    @Inject
    lateinit var customTabs: CustomTabs

    @Inject
    lateinit var navGraph: RootNavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme(themeManager) {
                CompositionLocalProvider(LocalCustomTabs provides customTabs) {
                    AppRoot(navGraph = navGraph, navigation = navigation, customTabs = customTabs)
                }
            }
        }
    }

    @Composable
    private fun AppTheme(themeManager: ThemeManager, content: @Composable () -> Unit) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val themeFlow = themeManager.observeSelectedTheme()
        val themeFlowLifecycleAware = remember(themeFlow, lifecycleOwner) {
            themeFlow.flowWithLifecycle(lifecycleOwner.lifecycle, STARTED)
        }

        val theme by themeFlowLifecycleAware.collectAsState(themeManager.getSelectedTheme())

        AppTheme(darkTheme = isAppInDarkTheme(theme)) {
            content()
        }
    }

    @Composable
    @ReadOnlyComposable
    private fun isAppInDarkTheme(theme: Theme): Boolean {
        return when (theme) {
            SYSTEM_DEFAULT -> isSystemInDarkTheme()
            LIGHT -> false
            DARK -> true
        }
    }
}
