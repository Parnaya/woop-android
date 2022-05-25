package com.example.woopchat.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.woopchat.MainActivity
import com.tinder.scarlet.Lifecycle
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import javax.inject.Provider
import javax.inject.Singleton

@Subcomponent(modules = [MainModule::class])
@MainScope
interface MainComponent { //TODO try provide this component through subcomponents in module
    fun inject(mainActivity: MainActivity)

    fun chatComponent(): ChatComponent
}