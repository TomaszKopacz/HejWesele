package com.hejwesele.android.splashscreen

import android.animation.ObjectAnimator
import android.app.Activity
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

object Splash {

    fun Activity.initializeSplashScreen() {
        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val fadeOut = ObjectAnimator.ofFloat(splashScreenViewProvider.view, View.ALPHA, 1f, 0f)
            fadeOut.interpolator = AccelerateInterpolator()
            fadeOut.duration = ANIMATION_DURATION

            fadeOut.doOnEnd { splashScreenViewProvider.remove() }
            fadeOut.start()
        }
    }

    private const val ANIMATION_DURATION = 200L
}
