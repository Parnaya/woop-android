package com.example.woopchat.di

import androidx.lifecycle.ViewModel
import com.example.woopchat.MainVimo
import com.example.woopchat.base.BaseVimo
import com.example.woopchat.service.ISocketUseCases
import com.example.woopchat.service.SocketUseCases
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {
    @Binds
    abstract fun provideSocketUseCases(socketUseCases: SocketUseCases): ISocketUseCases
}