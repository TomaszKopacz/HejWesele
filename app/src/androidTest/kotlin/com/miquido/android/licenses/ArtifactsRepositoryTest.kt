package com.miquido.android.licenses

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class ArtifactsRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: ArtifactsRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testLoadingArtifacts() = runTest {
        val artifacts = repository.loadArtifacts()
        assertThat(artifacts).isNotEmpty()
        assertThat(artifacts.first()).isInstanceOf(Artifact::class.java)
    }
}
