package com.miquido.android.database

import android.content.Context
import com.miquido.androidtemplate.database.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Suppress("unused")
internal class SqlDelightDatabaseProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : DatabaseProvider {

    override fun provideDatabase(name: String): Database {
        val driver = AndroidSqliteDriver(Database.Schema, context, name)
        return Database(driver)
    }
}
