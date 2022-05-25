package com.example.woopchat.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.woopchat.ChatVimo
import com.example.woopchat.MainVimo
import com.example.woopchat.service.SocketUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class VimoModule {
    @Binds
    @IntoMap
    @VimoKey(MainVimo::class)
    abstract fun bindMainVimo(vimo: MainVimo): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: VimoFactory): ViewModelProvider.Factory

    companion object {
        @Provides
        @IntoMap
        @VimoKey(ChatVimo::class)
        fun provideChatVimo(
            socketUseCases: SocketUseCases,
        ): ViewModel {
            return ChatVimo(
                "userTag",
                "chatTag",
                socketUseCases,
            )
        }
    }
}
