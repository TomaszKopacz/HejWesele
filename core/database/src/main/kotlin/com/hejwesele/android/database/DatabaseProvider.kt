package com.hejwesele.android.database

import com.hejwesele.database.Database

interface DatabaseProvider {
    fun provideDatabase(name: String): Database

    companion object Files {
        const val DEFAULT = "default.db"
    }
}
