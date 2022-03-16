plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("module-plugin") {
            id = "module-plugin"
            implementationClass = "CommonModulePlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:7.1.2")
    implementation(kotlin("gradle-plugin", "1.5.31"))
}

configurations.all {
    resolutionStrategy.eachDependency {
        when (requested.name) {
            "kotlin-reflect",
            "kotlin-stdlib",
            "kotlin-stdlib-common",
            "kotlin-stdlib-jdk7",
            "kotlin-stdlib-jdk8" -> useVersion("1.5.31")
        }
    }
}
