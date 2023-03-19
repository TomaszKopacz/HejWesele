package com.hejwesele

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.splashscreen.Splash.initializeSplashScreen
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.thememanager.ThemeManager
import com.hejwesele.navigation.AppNavigation
import com.hejwesele.settings.model.Theme
import com.hejwesele.settings.model.Theme.DARK
import com.hejwesele.settings.model.Theme.LIGHT
import com.hejwesele.settings.model.Theme.SYSTEM_DEFAULT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val themeFlow = themeManager.observeSelectedTheme()
            val themeFlowLifecycleAware = remember(themeFlow, lifecycleOwner) {
                themeFlow.flowWithLifecycle(lifecycleOwner.lifecycle, STARTED)
            }
            val theme by themeFlowLifecycleAware.collectAsState(themeManager.getSelectedTheme())

            AppTheme(darkTheme = isAppInDarkTheme(theme)) {
                SystemBarsColor(useDarkIcons = !isAppInDarkTheme(theme))
                AppNavigation()
            }
        }
    }

    @Composable
    private fun SystemBarsColor(useDarkIcons: Boolean) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
        }
    }

    @Composable
    @ReadOnlyComposable
    private fun isAppInDarkTheme(theme: Theme): Boolean = when (theme) {
        SYSTEM_DEFAULT -> isSystemInDarkTheme()
        LIGHT -> false
        DARK -> true
    }
}
