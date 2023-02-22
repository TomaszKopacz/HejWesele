package com.hejwesele.android.root

import androidx.compose.runtime.Composable
import com.hejwesele.android.customtabs.CustomTabs
import com.hejwesele.android.navigation.Navigation
import com.hejwesele.android.thememanager.ThemeManager
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

internal interface AppRootProvider {

    @Composable
    fun Root()
}

@ActivityScoped
internal class AppRootProviderImpl @Inject constructor(
    private val navGraph: RootNavGraph,
    private val navigation: Navigation,
    private val customTabs: CustomTabs,
    private val themeManager: ThemeManager,
) : AppRootProvider {

    @Composable
    override fun Root() {
        AppRoot(
            navGraph = navGraph,
            navigation = navigation,
            customTabs = customTabs,
            themeManager = themeManager
        )
    }
}
