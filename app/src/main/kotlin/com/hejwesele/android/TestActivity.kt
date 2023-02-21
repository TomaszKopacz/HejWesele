package com.hejwesele.android

import androidx.activity.ComponentActivity
import com.hejwesele.android.customtabs.CustomTabs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestActivity : ComponentActivity() {

    @Inject
    lateinit var customTabs: CustomTabs
}
