@file:Suppress("UnstableApiUsage")
import Dependencies.*
import com.android.build.api.variant.impl.VariantOutputImpl
import com.google.protobuf.gradle.*

plugins {
    androidApplication()
    kotlinAndroid()
    modulePlugin()
    protobuf()
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
            protobuf {
                java.srcDir("${generatedFilesBaseDir}/main/javalite")
            }
            java.srcDir("$projectDir/src/main/proto")
            java.srcDir("$projectDir/src/main/protobuffers")
        }
    }

//    tasks.withType<ProcessResources> {
//        exclude("**/*.proto")
//    }

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
        Libs.Protoc,
        Libs.ProtobufJava,
        Libs.ProtobufKotlin,
        Libs.ProtobufJavalite,
        Libs.ProtobufLite,
    )
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.Libs.protobuf}"
    }
    plugins {
//        javalite {
//
//        }
    }
//    plugins {
//        javalite {
//            artifact = 'com.google.protobuf:protoc-gen-javalite:3.0.0'
//        }
//    }

    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("kotlin")
            }
            it.plugins {
                val hh = 7
            }
        }
    }
}

configurations.forEach { configuration ->
    if (configuration.name.toLowerCase().contains("proto")) {
        configuration.attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, "java-runtime"))
    }
}

project.tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
    mustRunAfter(project.tasks.withType(com.google.protobuf.gradle.GenerateProtoTask::class.java))
}

println(System.getenv())