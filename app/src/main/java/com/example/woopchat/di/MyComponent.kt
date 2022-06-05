package com.example.woopchat.di

import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@DefineComponent(parent = SingletonComponent::class)
interface MyComponent

@DefineComponent.Builder
interface MyComponentBuilder {
    fun build(): MyComponent
}
