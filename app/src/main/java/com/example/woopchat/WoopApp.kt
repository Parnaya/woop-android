package com.example.woopchat

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

class WoopApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

//    companion object {
//        lateinit var context: Context
//    }
}