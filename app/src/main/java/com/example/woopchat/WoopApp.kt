package com.example.woopchat

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WoopApp: Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

    companion object {
        lateinit var instance: WoopApp
    }
}