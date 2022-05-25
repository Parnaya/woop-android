package com.example.woopchat.di

import android.app.Application
import android.content.res.AssetManager
import com.example.woopchat.BuildConfig
import com.example.woopchat.base.AppConfig
import com.example.woopchat.base.BuildVariant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideAssetManager(application: Application): AssetManager {
        return application.assets
    }

    @Provides
    @Singleton
    fun provideAppConfig(application: Application): AppConfig {
        return AppConfig(
            buildVariant = when (BuildConfig.BUILD_TYPE) {
                "release" -> BuildVariant.Release
                else -> BuildVariant.Debug
            }
        )
    }
}