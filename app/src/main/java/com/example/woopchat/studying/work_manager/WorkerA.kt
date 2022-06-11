package com.example.woopchat.studying.work_manager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.example.woopchat.studying.concurrency.logThread
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay

class WorkerA(
    private val context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result { //runs asynchronously on a background thread provided by WorkManager
        logThread("======== WorkerA doWork")
        delay(100)
//        Thread.sleep(3000)
//        Thread.yield()
//        Thread.sleep(3000)
//        Thread.yield()

//        Toast.makeText(context, inputData.getString(Text), Toast.LENGTH_LONG).show()
        setProgressAsync(workDataOf(
            Res to "some res"
        ))
        Result.retry()
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    companion object {
        const val Text = "WorkerAText"
        const val Res = "WorkerAResult"
    }
}