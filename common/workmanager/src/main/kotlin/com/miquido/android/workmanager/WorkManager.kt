package com.miquido.android.workmanager

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.Operation.State.SUCCESS
import androidx.work.WorkInfo
import kotlinx.coroutines.Deferred
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

interface WorkManager {

    fun <T : ListenableWorker> scheduleOneTimeWork(
        worker: KClass<T>,
        tag: String,
        constraints: Constraints = Constraints.NONE,
        inputData: Data = Data.EMPTY,
        delay: TimeInterval? = null
    ): Deferred<SUCCESS>

    fun <T : ListenableWorker> schedulePeriodicWork(
        worker: KClass<T>,
        uniqueWorkName: String,
        repeatInterval: TimeInterval,
        flexInterval: TimeInterval,
        constraints: Constraints = Constraints.NONE,
        inputData: Data = Data.EMPTY,
        delay: TimeInterval? = null,
        existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE
    ): Deferred<SUCCESS>

    fun getWorkInfosByTag(tag: String): Deferred<List<WorkInfo>>
}

data class TimeInterval(val value: Long, val unit: TimeUnit)
