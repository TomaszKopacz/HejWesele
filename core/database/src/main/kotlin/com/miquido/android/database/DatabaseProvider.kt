package com.miquido.android.database

import com.miquido.androidtemplate.database.Database

interface DatabaseProvider {
    fun provideDatabase(name: String): Database

    companion object Files {
        const val DEFAULT = "default.db"
    }
}
