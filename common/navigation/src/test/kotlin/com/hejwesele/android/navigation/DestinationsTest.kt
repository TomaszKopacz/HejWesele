package com.hejwesele.android.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DestinationsTest {

    @Test
    fun `resolve destination for authentication correctly`() {
        val result = Destinations.authentication

        assertThat(result.route).isEqualTo("authentication")
    }

    @Test
    fun `resolve destination for main correctly`() {
        val result = Destinations.main(userId = 1234)

        assertThat(result.route).isEqualTo("main/1234")
    }
}
