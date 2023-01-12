package com.hejwesele.android.database.di

import com.hejwesele.android.database.DatabaseProvider.Files.DEFAULT
import com.hejwesele.android.database.SqlDelightSqlCipherDatabaseProvider
import com.hejwesele.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(provider: SqlDelightSqlCipherDatabaseProvider): Database {
        return provider.provideDatabase(DEFAULT)
    }
}
