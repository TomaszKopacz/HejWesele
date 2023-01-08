package com.miquido.android.database.di

import com.miquido.android.database.DatabaseProvider.Files.DEFAULT
import com.miquido.android.database.SqlDelightSqlCipherDatabaseProvider
import com.miquido.androidtemplate.database.Database
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
