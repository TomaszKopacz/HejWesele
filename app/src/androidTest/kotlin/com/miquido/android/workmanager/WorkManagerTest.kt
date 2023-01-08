package com.miquido.android.workmanager

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.Operation.State.SUCCESS
import androidx.work.WorkInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class WorkManagerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var workManager: WorkManager

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun testOneTimeWorker() = runBlocking {
        val tag = "TAG_TEST"
        val inputData = workDataOf("key_1" to 1, "key_2" to 2)
        val result: SUCCESS
        val workInfo: WorkInfo

        runBlocking {
            result = workManager.scheduleOneTimeWork(EchoWorker::class, inputData = inputData, tag = tag).await()
            workInfo = workManager.getWorkInfosByTag(tag).await().first()
        }

        assertThat(result).isInstanceOf(SUCCESS::class.java)
        assertThat(workInfo.state).isEqualTo(WorkInfo.State.SUCCEEDED)
        assertThat(workInfo.outputData).isEqualTo(inputData)
    }

    @Test
    fun testPeriodicWork() = runBlocking {
        val tag = "TAG_TEST"
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        val result: SUCCESS
        val workInfo: WorkInfo

        runBlocking {
            result = workManager.schedulePeriodicWork(
                EchoWorker::class, tag, TimeInterval(1, TimeUnit.HOURS), TimeInterval(15, TimeUnit.MINUTES)
            ).await()

            workInfo = workManager.getWorkInfosByTag(tag).await().first()
        }

        testDriver?.setPeriodDelayMet(workInfo.id)

        assertThat(result).isInstanceOf(SUCCESS::class.java)
        assertThat(workInfo.state).isEqualTo(WorkInfo.State.ENQUEUED)
    }
}

class EchoWorker(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {
    override fun doWork(): Result {
        return when (inputData.size()) {
            0 -> Result.failure()
            else -> Result.success(inputData)
        }
    }
}
