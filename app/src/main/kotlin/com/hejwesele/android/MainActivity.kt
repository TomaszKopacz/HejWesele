package com.hejwesele.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.customtabs.CustomTabs
import com.hejwesele.android.customtabs.LocalCustomTabs
import com.hejwesele.android.navigation.EventDirection
import com.hejwesele.android.navigation.EventDirection.getEventId
import com.hejwesele.android.navigation.Navigation
import com.hejwesele.android.navigation.composable
import com.hejwesele.android.navigation.getStartDestination
import com.hejwesele.android.navigation.navigate
import com.hejwesele.android.splashscreen.Splash.initializeSplashScreen
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Transitions
import com.hejwesele.android.thememanager.Theme
import com.hejwesele.android.thememanager.Theme.DARK
import com.hejwesele.android.thememanager.Theme.LIGHT
import com.hejwesele.android.thememanager.Theme.SYSTEM_DEFAULT
import com.hejwesele.android.thememanager.ThemeManager
import com.hejwesele.event.Event
import com.hejwesele.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
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

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme(themeManager) {
                SystemBarsColor()
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
    private fun SystemBarsColor() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight
        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
        }
    }

    @Composable
    private fun Content() {
        val navController = rememberAnimatedNavController()
        val onboardingDisplayedState by viewModel.getOnboardingDisplayed().collectAsState(initial = false)

        LaunchedEffect(navController) {
            navigation.events.collect(navController::navigate)
        }

        AnimatedNavHost(
            navController = navController,
            enterTransition = { Transitions.fadeIn },
            exitTransition = { Transitions.fadeOut },
            startDestination = getStartDestination(onboardingDisplayedState)
        ) {
            composable(
                direction = EventDirection,
                enterTransition = { Transitions.slideInHorizontally }
            ) { backStackEntry ->
                Event(
                    eventId = backStackEntry.arguments?.getEventId() ?: 0
                )
            }
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