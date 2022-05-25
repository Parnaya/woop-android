package com.example.woopchat.di

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.woopchat.ChatFragment
import com.example.woopchat.MainActivity
import com.tinder.scarlet.Lifecycle
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton


@Subcomponent(modules = [ChatModule::class])
@ChatScope
interface ChatComponent { //TODO try provide this component through subcomponents in module
    fun inject(fragment: ChatFragment)
}
