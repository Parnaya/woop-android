import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class CommonModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidExtensions = project.extensions.getByName("android")
        // configure the android block
        if (androidExtensions is BaseExtension) {
            androidExtensions.apply {
                buildToolsVersion = Versions.Build.tools
                compileSdkVersion(Versions.Build.compileSdk)

                defaultConfig {
                    minSdk = Versions.Build.minSdk
                    targetSdk = Versions.Build.targetSdk

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                project.tasks.withType(KotlinCompile::class.java).configureEach {
                    kotlinOptions {
                        jvmTarget = Versions.java
                    }
//                    dependsOn(":generateProto")
                }

                if (this is LibraryExtension) {
                    defaultConfig {
                        consumerProguardFile("consumer-rules.pro")
                    }
//                    buildTypes {
//                        create("dev")
//                    }
                }
            }
        }
    }
}
