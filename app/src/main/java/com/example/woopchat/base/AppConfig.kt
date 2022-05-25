package com.example.woopchat.base

class AppConfig(
    val buildVariant: BuildVariant,
) {
    fun isDebug() = buildVariant != BuildVariant.Release
}

enum class BuildVariant {
    Debug,
    Release,
}