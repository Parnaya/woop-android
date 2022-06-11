package com.example.woopchat.studying.experiment

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import com.example.woopchat.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class StartedService : Service() {
    val coroutineScope = CoroutineScope(
        SupervisorJob() +
        Dispatchers.bg
    ) //

//    val coroutineScopee = supervisorScope {
//
//    }

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelfResult(msg.arg1)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        Log.d("===============", "StartedService: onStartCommand")
        START_STICKY
        START_REDELIVER_INTENT
        START_STICKY_COMPATIBILITY
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("===============", "StartedService: onBind")
        return null //for only started service
    }

    override fun onCreate() {
        Log.d("===============", "StartedService: onCreate")

//        coroutineScope.async {  }.await()



        //The system invokes this method to perform one-time setup procedures
        //when the service is initially created (before it calls either onStartCommand() or onBind())
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        Log.d("===============", "StartedService: onDestroy")
        super.onDestroy()
    }
}