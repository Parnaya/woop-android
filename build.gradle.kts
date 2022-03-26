// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            when (requested.name) {
                "activity" -> useVersion("1.3.1")
                "activity-ktx" -> useVersion("1.3.1")
                "annotation" -> useVersion(Versions.Libs.annotation)
                "appcompat" -> useVersion(Versions.Libs.appcompat)
                "appcompat-resources" -> useVersion(Versions.Libs.appcompat)
                "collection" -> useVersion("1.1.0")
                "collection-ktx" -> useVersion("1.1.0")
                "constraintlayout" -> useVersion(Versions.Libs.constraintLayout)
                "coordinatorlayout" -> useVersion(Versions.Libs.coordinatorLayout)
                "exifinterface" -> useVersion(Versions.Libs.exifinterface)
                "fragment" -> useVersion(Versions.Libs.fragmentKtx)
                "glide" -> useVersion(Versions.Libs.glide)
                "gson" -> useVersion(Versions.Libs.gson)
                "lifecycle-common" -> useVersion(Versions.Libs.lifecycle)
                "lifecycle-livedata" -> useVersion(Versions.Libs.lifecycle)
                "lifecycle-livedata-core" -> useVersion(Versions.Libs.lifecycle)
                "lifecycle-livedata-core-ktx" -> useVersion(Versions.Libs.lifecycle)
                "lifecycle-runtime" -> useVersion(Versions.Libs.lifecycle)
                "lifecycle-runtime-ktx" -> useVersion(Versions.Libs.lifecycle)
                "lifecycle-viewmodel" -> useVersion(Versions.Libs.lifecycle)
                "lifecycle-viewmodel-ktx" -> useVersion(Versions.Libs.lifecycle)
                "recyclerview" -> useVersion(Versions.Libs.recyclerView)
                "transition" -> useVersion(Versions.Libs.transition)
            }
            if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-stdlib")) {
                useVersion(Versions.kotlin)
            }
            if (requested.group == "org.jetbrains.kotlinx" && requested.name.startsWith("kotlinx-coroutines")) {
                useVersion(Versions.Libs.coroutines)
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}