package com.miquido.androidtemplate.rules

import android.app.Activity
import androidx.test.ext.junit.rules.ActivityScenarioRule

inline fun <reified A : Activity> activityScenarioRule() = ActivityScenarioRule(A::class.java)
