package com.miquido.androidtemplate

import com.miquido.androidtemplate.authentication.AuthenticationTest
import com.miquido.androidtemplate.dashboard.DashboardTest
import com.miquido.androidtemplate.main.MainTest
import com.miquido.androidtemplate.settings.SettingsTest
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(
    AuthenticationTest::class,
    DashboardTest::class,
    MainTest::class,
    SettingsTest::class
)
class UiTestsSuite
