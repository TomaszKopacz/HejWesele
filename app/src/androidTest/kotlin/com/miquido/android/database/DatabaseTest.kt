package com.miquido.android.database

import com.google.common.truth.Truth.assertThat
import com.miquido.android.InstrumentedUnitTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@InstrumentedUnitTest
class DatabaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: Database

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testSampleQueriesSelectAll() {
        val queries = database.sampleQueries
        val elements = queries.selectAll().executeAsList()
        assertThat(elements).hasSize(1)
        assertThat(elements.first()).isEqualTo(Sample(id = 15, name = "Miquido Mayor"))
    }
}
