import org.gradle.plugin.use.PluginDependenciesSpec

fun PluginDependenciesSpec.androidApplication() {
    id("com.android.application")
}

fun PluginDependenciesSpec.androidLibrary() {
    id("com.android.library")
}

fun PluginDependenciesSpec.kotlinAndroid() {
    id("kotlin-android")
}

fun PluginDependenciesSpec.modulePlugin() {
    id("module-plugin")
}

fun PluginDependenciesSpec.parcelize() {
    id("kotlin-parcelize")
}

fun PluginDependenciesSpec.kapt() {
    id("kotlin-kapt")
}

fun PluginDependenciesSpec.navigationSafeArgs() {
    id("androidx.navigation.safeargs.kotlin")
}

fun PluginDependenciesSpec.crashlytics() {
    id("com.google.firebase.crashlytics")
}
