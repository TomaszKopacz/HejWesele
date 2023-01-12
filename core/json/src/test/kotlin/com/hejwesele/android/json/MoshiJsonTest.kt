package com.hejwesele.android.json

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.JsonClass
import org.junit.Test
import java.util.Date

class MoshiJsonTest {

    private val json: Json = MoshiJson()

    @JsonClass(generateAdapter = true)
    data class ClassUnderTest(
        val testString: String,
        val testDate: Date
    )

    @Test
    fun `test simple object serialization`() {
        val clazz = ClassUnderTest(testString = "Test", testDate = Date(1))
        val expected = "{\"testString\":\"Test\",\"testDate\":\"1970-01-01T00:00:00.001Z\"}"
        val actual = json.toJson(clazz)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test simple object deserialization`() {
        val jsonText = "{\"testString\":\"Test\",\"testDate\":\"1970-01-01T00:00:00.001Z\"}"
        val expected = ClassUnderTest(testString = "Test", testDate = Date(1))
        val actual = json.fromJson<ClassUnderTest>(jsonText)

        assertThat(actual).isEqualTo(expected)
    }
}
