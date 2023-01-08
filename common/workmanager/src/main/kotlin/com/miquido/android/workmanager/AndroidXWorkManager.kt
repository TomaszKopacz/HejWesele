package com.miquido.android.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation.State.SUCCESS
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.guava.asDeferred
import javax.inject.Inject
import kotlin.reflect.KClass

internal class AndroidXWorkManager @Inject constructor(
    @ApplicationContext private val context: Context
) : WorkManager {

    private val workManager: androidx.work.WorkManager by lazy {
        androidx.work.WorkManager.getInstance(context)
    }

    override fun <T : ListenableWorker> scheduleOneTimeWork(
        worker: KClass<T>,
        tag: String,
        constraints: Constraints,
        inputData: Data,
        delay: TimeInterval?
    ): Deferred<SUCCESS> {
        return OneTimeWorkRequest.Builder(worker.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(tag)
            .apply {
                if (delay != null) {
                    setInitialDelay(delay.value, delay.unit)
                }
            }
            .let { workManager.enqueue(it.build()).result.asDeferred() }
    }

    override fun <T : ListenableWorker> schedulePeriodicWork(
        worker: KClass<T>,
        uniqueWorkName: String,
        repeatInterval: TimeInterval,
        flexInterval: TimeInterval,
        constraints: Constraints,
        inputData: Data,
        delay: TimeInterval?,
        existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy
    ): Deferred<SUCCESS> {
        return PeriodicWorkRequest
            .Builder(
                worker.java,
                repeatInterval.value,
                repeatInterval.unit,
                flexInterval.value,
                flexInterval.unit
            )
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(uniqueWorkName)
            .apply {
                if (delay != null) {
                    setInitialDelay(delay.value, delay.unit)
                }
            }
            .let {
                workManager
                    .enqueueUniquePeriodicWork(uniqueWorkName, existingPeriodicWorkPolicy, it.build())
                    .result
                    .asDeferred()
            }
    }

    override fun getWorkInfosByTag(tag: String): Deferred<List<WorkInfo>> {
        return workManager.getWorkInfosByTag(tag).asDeferred()
    }
}
