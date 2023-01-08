package com.miquido.android

import com.miquido.android.analytics.AnalyticsTest
import com.miquido.android.database.DatabaseTest
import com.miquido.android.datetimeformatter.DateTimeFormatterTest
import com.miquido.android.licenses.ArtifactsRepositoryTest
import com.miquido.android.messaging.MessagingTest
import com.miquido.android.preferences.PreferencesTest
import com.miquido.android.remoteconfig.RemoteConfigTest
import com.miquido.android.workmanager.WorkManagerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(
    AnalyticsTest::class,
    ArtifactsRepositoryTest::class,
    DatabaseTest::class,
    DateTimeFormatterTest::class,
    MessagingTest::class,
    PreferencesTest::class,
    RemoteConfigTest::class,
    WorkManagerTest::class
)
class InstrumentedUnitTestsSuite
