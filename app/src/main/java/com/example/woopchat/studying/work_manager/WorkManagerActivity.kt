package com.example.woopchat.studying.work_manager

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.woopchat.utils.MATCH_PARENT
import java.util.concurrent.TimeUnit

class WorkManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            background = ColorDrawable(Color.DKGRAY)
        }.also(::setContentView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        Log.d("========", "activity onStart")

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val requestA = OneTimeWorkRequestBuilder<WorkerA>()
            .setInputData(
                workDataOf(
                    WorkerA.Text to "That message form MyWorker",
                )
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        val requestB1 = OneTimeWorkRequestBuilder<WorkerB>()
            .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
            .build()
            .apply {  }
        val requestB2 = OneTimeWorkRequestBuilder<WorkerB>().build()
        val requestC = OneTimeWorkRequestBuilder<WorkerC>()
            .setInputMerger(ArrayCreatingInputMerger::class.java)
            .build()

        val periodicRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<WorkerA>(9, TimeUnit.SECONDS)
            .build()

        Constraints
            .Builder()
            .setRequiresDeviceIdle(false)
            .addContentUriTrigger(Uri.EMPTY, false)
            .build()


        val workManager = WorkManager
//            .initialize(this, Configuration.Builder().build())
            .getInstance(this)




        val status: LiveData<WorkInfo> = workManager.getWorkInfoByIdLiveData(requestA.id)
        status.observe(this) { info ->
            info.progress
            Log.d("========", "${info.id} : ${info.state}")
//            when (info.state) {
//                WorkInfo.State.ENQUEUED -> TODO()
//                WorkInfo.State.RUNNING -> TODO()
//                WorkInfo.State.SUCCEEDED -> TODO()
//                WorkInfo.State.FAILED -> TODO()
//                WorkInfo.State.BLOCKED -> TODO()
//                WorkInfo.State.CANCELLED -> TODO()
//            }
        }

        val res = workManager
            .beginWith(requestA)
            .then(listOf(requestB1, requestB2))
            .then(requestC)
            .enqueue()
            .result
//        workManager
//            .beginUniqueWork("uniqueWorkName", ExistingWorkPolicy.APPEND, oneTimeRequest)
//            .enqueue()


        //.enqueue(oneTimeRequest)
    }
}
