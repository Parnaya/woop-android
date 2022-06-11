package com.example.woopchat.studying.work_manager

import android.app.Notification
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.example.woopchat.base.Generator
import com.example.woopchat.studying.concurrency.logThread
import com.example.woopchat.studying.work_manager.WorkerA.Companion.Res
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay


class WorkerB(
    private val context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result { //runs asynchronously on a background thread provided by WorkManager
        logThread("======== WorkerB doWork")
        delay(100)
//        Thread.sleep(3000)
//        Thread.yield()
//        Thread.sleep(3000)
//        Thread.yield()

//        Toast.makeText(context, inputData.getString(Text), Toast.LENGTH_LONG).show()
        Result.retry()
//        return Result.failure()
        return Result.success(workDataOf(Res to Generator.randomString()))
    }

    /** notification to show for the user
     * */
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            1234,
            Notification(

            )
        )
    }

    companion object {
        const val Text = "WorkerBText"
        const val Res = "WorkerBRes"
    }
}