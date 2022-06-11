package com.example.woopchat.studying.work_manager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.woopchat.studying.concurrency.logThread
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay

class WorkerC(
    private val context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result { //runs asynchronously on a background thread provided by WorkManager
        logThread("======== WorkerC doWork, input: ${inputData.getStringArray(WorkerB.Res)?.joinToString()}")
        delay(100)
//        Thread.sleep(3000)
//        Thread.yield()
//        Thread.sleep(3000)
//        Thread.yield()

//        Toast.makeText(context, inputData.getString(Text), Toast.LENGTH_LONG).show()
        Result.retry()
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    companion object {
        const val Text = "WorkerCText"
    }
}