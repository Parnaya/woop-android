package com.example.woopchat.studying.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.woopchat.utils.MATCH_PARENT


class BroadcastReceiverActivity : AppCompatActivity() {

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
        contextReceiver.register(this)
        Log.d("========", "activity onStart")
    }
}

private val contextReceiver = object : BroadcastReceiver() {
    /** This method is always called within the main thread of its process,
     *  unless you explicitly asked for it to be scheduled on a different thread */
    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            "android.net.conn.CONNECTIVITY_CHANGE" -> isOnline(context)
            "android.intent.action.AIRPLANE_MODE" -> TODO()
        }
        intent.categories.forEach {

        }
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }

    fun register(activity: AppCompatActivity) {
        val intentFilter = IntentFilter().apply {
            addAction("android.net.conn.CONNECTIVITY_CHANGE")
        }

        activity.registerReceiver(this, intentFilter)
    }

    fun unregister(activity: AppCompatActivity) {
        activity.unregisterReceiver(this)
    }
}


