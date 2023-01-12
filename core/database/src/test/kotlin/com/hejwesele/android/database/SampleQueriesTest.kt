package com.hejwesele.android.database

import com.google.common.truth.Truth.assertThat
import com.hejwesele.database.Database
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SampleQueriesTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(IN_MEMORY).apply {
        Database.Schema.create(this)
    }

    private val queries = Database(inMemorySqlDriver).sampleQueries

    @Test
    fun `test sample selectAll`() {
        val elements = queries.selectAll().executeAsList()
        assertThat(elements).hasSize(1)
        assertThat(elements.first()).isEqualTo(Sample(id = 15, name = "Miquido Mayor"))
    }

    @Test
    fun `test sample selectAll using coroutines`() {
        val elements: List<Sample>
        runBlocking { elements = queries.selectAll().asFlow().mapToList().first() }
        assertThat(elements).hasSize(1)
        assertThat(elements.first()).isEqualTo(Sample(id = 15, name = "Miquido Mayor"))
    }
}
