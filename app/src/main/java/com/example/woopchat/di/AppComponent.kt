package com.example.woopchat.di

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
@Component(modules = [AppModule::class, VimoModule::class, NetworkModule::class])
interface AppComponent {

    fun mainComponent(): MainComponent

    fun getVimoFactory(): VimoFactory

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}