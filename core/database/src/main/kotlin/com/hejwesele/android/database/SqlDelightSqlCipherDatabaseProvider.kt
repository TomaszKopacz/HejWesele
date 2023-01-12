package com.hejwesele.android.database

import android.content.Context
import com.hejwesele.database.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.hilt.android.qualifiers.ApplicationContext
import net.sqlcipher.database.SupportFactory
import javax.inject.Inject

internal class SqlDelightSqlCipherDatabaseProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val passphraseProvider: PassphraseProvider
) : DatabaseProvider {

    override fun provideDatabase(name: String): Database {
        val factory = SupportFactory(passphraseProvider.providePassphrase(name))
        val driver = AndroidSqliteDriver(Database.Schema, context, name, factory)
        return Database(driver)
    }
}
