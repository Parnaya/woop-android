@file:Suppress("UnstableApiUsage")
import Dependencies.Libs
import com.android.build.api.variant.impl.VariantOutputImpl

plugins {
    androidApplication()
    kotlinAndroid()
    modulePlugin()
    kapt()
    hilt()
}

println("build arguments: $BuildArgs")

android {
    defaultConfig {
        applicationId = BuildArgs.applicationId
        versionCode = BuildArgs.versionCode
        versionName = BuildArgs.versionName
        testInstrumentationRunnerArguments.putIfAbsent("clearPackageData", "true")
        buildConfigField("String", "STAGING_PRIVATE_API_URL", "\"${BuildArgs.stagingPrivateApiUrl}\"")
    }

    buildTypes {
        getByName("debug") {
            javaCompileOptions.annotationProcessorOptions.arguments["dagger.hilt.disableModulesHaveInstallInCheck"] = "true"
            isDebuggable = true
            applicationIdSuffix = ".dev"
            resValue("bool", "is_debug", "true")
            resValue("string", "app_name", "Woop Debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("bool", "is_debug", "false")
            resValue("string", "app_name", "Woop")
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/java")
            aidl.srcDir("src/main/java")
        }
    }

    buildFeatures.viewBinding = true
}

androidComponents.onVariants { variant ->
    val outputFileNamePrefix = "woop"
    val variantName = variant.name
    val variantOutput = variant.outputs.first() as VariantOutputImpl
    val variantVersion = variantOutput.versionName.orNull
    val fileName = "${outputFileNamePrefix}_${variantName}_${variantVersion}"
    val apkFileName = "$fileName.apk"
    variantOutput.outputFileName.set(apkFileName)
    val taskSuffix = variantName.capitalize()
    val bundleTaskName = "bundle${taskSuffix}"
    afterEvaluate {
        tasks.findByName(bundleTaskName)?.apply {
            val oldAabName = "app-${variantName}.aab"
            val newAabName = "${fileName}.aab"
            val renameAabTaskName = "renameAab${taskSuffix}"
            project.tasks.create(renameAabTaskName, Copy::class.java) {
                description = "Renames AAB"
                includeEmptyDirs = false
                val bundleDir = "${project.buildDir}/outputs/bundle/${variantName}"
                from(bundleDir) {
                    include(oldAabName)
                }
                into(bundleDir)
                rename(oldAabName, newAabName)
                val cleanTaskName = "cleanAfterRename${taskSuffix}"
                project.tasks.create(cleanTaskName, Delete::class.java) {
                    delete("${bundleDir}/${oldAabName}")
                }
                finalizedBy(cleanTaskName)
            }
            finalizedBy(renameAabTaskName)
        }
    }
}

dependencies {
    implementation(
        Libs.CoreKtx,
        Libs.Appcompat,
        Libs.Material,
        Libs.ConstraintLayout,
        Libs.Scarlet,
        Libs.ScarletOkhttp,
        Libs.ScarletAdapterCoroutines,
        Libs.ScarletAdapterProtobuf,
        Libs.ScarletLifecycleAndroid,
        Libs.ScarletAdapterGson,
        Libs.Kotlin,
        Libs.Coroutines,
        Libs.Okhttp,
        Libs.LoggingInterceptor,
        Libs.LifecycleCommon,
        Libs.LifecycleLiveDataKtx,
        Libs.LifecycleRuntimeKtx,
        Libs.LifecycleViewModelKtx,
        Libs.FragmentKtx,
        Libs.Threetenabp,
        Libs.JsonKotlinSchema,
        Libs.ApacheCollections,
        Libs.WorkGsm,
        Libs.WorkMultiprocess,
        Libs.WorkRuntimeKtx,
        Libs.Dagger,
        Libs.DaggerAndroid,
        Libs.DaggerAndroidSupport,
        Libs.HiltAndroid,
        Libs.JavaPoet,
    )
    kapt(
        Libs.DaggerCompiler,
        Libs.DaggerAndroidProcessor,
        Libs.HiltCompiler,
    )
}

hilt {
    enableAggregatingTask = true
}

println(System.getenv())