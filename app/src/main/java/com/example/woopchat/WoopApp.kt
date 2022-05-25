package com.example.woopchat

import android.app.Application
import com.example.woopchat.di.AppComponent
import com.example.woopchat.di.ChatComponent
import com.example.woopchat.di.DaggerAppComponent
import com.example.woopchat.di.MainComponent
import com.jakewharton.threetenabp.AndroidThreeTen

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
        private var backAppComponent: AppComponent? = null
        private var backMainComponent: MainComponent? = null
        private var backChatComponent: ChatComponent? = null

        val AppComponent: AppComponent
            get() {
                if (backAppComponent == null) {
                    backAppComponent = DaggerAppComponent.builder().application(instance).build()
                }
                return backAppComponent!!
            }

        val MainComponent: MainComponent
            get() {
                if (backMainComponent == null) {
                    backMainComponent = AppComponent.mainComponent()
                }
                return backMainComponent!!
            }

        val ChatComponent: ChatComponent
            get() {
                if (backChatComponent == null) {
                    backChatComponent = MainComponent.chatComponent()
                }
                return backChatComponent!!
            }
    }
}