package com.miquido.androidtemplate

import androidx.activity.ComponentActivity
import com.miquido.android.customtabs.CustomTabs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestActivity : ComponentActivity() {

    @Inject
    lateinit var customTabs: CustomTabs
}
