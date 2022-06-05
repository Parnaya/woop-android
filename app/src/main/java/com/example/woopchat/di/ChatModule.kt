package com.example.woopchat.di

import androidx.lifecycle.ViewModel
import com.example.woopchat.ChatVimo
import com.example.woopchat.service.SocketUseCases
import dagger.BindsOptionalOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class ChatModule {
 //todo try multi scopes with Hilt

}