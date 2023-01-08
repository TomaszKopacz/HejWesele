package com.miquido.androidtemplate.util

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.Description
import org.junit.runner.notification.RunListener

class ToastingRunListener : RunListener() {

    override fun testStarted(description: Description) {
        val testName = description.displayName
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            Toast.makeText(context, testName, LENGTH_LONG).show()
        }
    }
}
