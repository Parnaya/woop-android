package com.example.woopchat.di

import androidx.lifecycle.ViewModel
import com.example.woopchat.ChatVimo
import com.example.woopchat.service.SocketUseCases
import dagger.BindsOptionalOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
class ModuleB {
    @Provides
    @ActivityRetainedScoped
    fun provideB(a: A) : B = B(a)
}

class B(
    val a: A,
)