package com.example.woopchat.studying.experiment

import android.app.Service
import android.content.Intent
import android.content.Intent.EXTRA_INTENT
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import com.example.woopchat.IExperimentAidl
import com.example.woopchat.coroutines.Dispatchers
import kotlinx.coroutines.*

class BoundService : Service() {

    val scope = CoroutineScope(Dispatchers.mainImmediate + SupervisorJob())
    var messenger: Messenger? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("===============", "BoundService: onStartCommand")
        START_STICKY
        START_NOT_STICKY
        START_REDELIVER_INTENT
        return START_STICKY_COMPATIBILITY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("===============", "BoundService: onBind")
        scope.launch {
            delay(1500)
            val nested = intent.extras?.getParcelable<Intent>(EXTRA_INTENT)
            startActivity(nested)
        }

        messenger = intent.extras?.getParcelable("messenger") as? Messenger

        val binder = object: IExperimentAidl.Stub() {
            override fun basicTypes(anInt: Int, aLong: Long, aBoolean: Boolean, aFloat: Float, aDouble: Double, aString: String?) {
                //here you cab launch smth work
            }

            override fun sum(first: Int, second: Int): Int {
                //here you cab launch smth work
                return 5 + 7
            }

        }
        return binder //for only started service
    }

    override fun onCreate() {
        Log.d("===============", "BoundService: onCreate")
        super.onCreate()
        //The system invokes this method to perform one-time setup procedures
        //when the service is initially created (before it calls either onStartCommand() or onBind())
    }

    override fun onDestroy() {
        Log.d("===============", "BoundService: onDestroy")
        scope.cancel()
        super.onDestroy()
    }
}