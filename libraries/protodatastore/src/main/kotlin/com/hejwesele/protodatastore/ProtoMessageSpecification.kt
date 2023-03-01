package com.hejwesele.protodatastore

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class ProtoMessageSpecification<T>(
    private val initialValue: T,
    private val reader: (InputStream) -> T,
    private val writer: (T, OutputStream) -> Unit
) {

    internal val serializer = object : Serializer<T> {
        override val defaultValue = initialValue

        override suspend fun readFrom(input: InputStream) = reader(input)

        override suspend fun writeTo(t: T, output: OutputStream) = writer(t, output)
    }
}
