import org.gradle.api.artifacts.ExternalModuleDependency

sealed class Dependencies(
    val path: String,
    val configuration: (ExternalModuleDependency.() -> Unit)? = null,
) {
    sealed class Commons(path: String) : Dependencies(path) {}

    sealed class Apis(path: String) : Dependencies(path) {}

    sealed class Widgets(path: String) : Dependencies(path) {}

    sealed class Features(path: String) : Dependencies(path) {}

    sealed class Libs(path: String, configuration: (ExternalModuleDependency.() -> Unit)? = null) : Dependencies(path, configuration) {
        //core
        object Kotlin : Libs("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")
        object KotlinJava : Libs("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
        object Coroutines : Libs("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Libs.coroutines}")

        //di
        object Koin : Libs("io.insert-koin:koin-android:${Versions.Libs.koin}")

        //date time
        object Threetenabp : Libs("com.jakewharton.threetenabp:threetenabp:${Versions.Libs.threetenabp}")

        //json parsing
        object Gson : Libs("com.google.code.gson:gson:${Versions.Libs.gson}")

        //networking
        object Okhttp : Libs("com.squareup.okhttp3:okhttp:${Versions.Libs.okhttp}")
        object LoggingInterceptor : Libs("com.squareup.okhttp3:logging-interceptor:${Versions.Libs.okhttp}")
        object Retrofit : Libs("com.squareup.retrofit2:retrofit:${Versions.Libs.retrofit}")
        object ConverterGson : Libs("com.squareup.retrofit2:converter-gson:${Versions.Libs.retrofit}")
        object Scarlet: Libs("com.tinder.scarlet:scarlet:${Versions.Libs.scarlet}")
        object ScarletOkhttp: Libs("com.tinder.scarlet:websocket-okhttp:${Versions.Libs.scarlet}")
        object ScarletLifecycleAndroid: Libs("com.tinder.scarlet:lifecycle-android:${Versions.Libs.scarlet}")
        object ScarletAdapterCoroutines: Libs("com.tinder.scarlet:stream-adapter-coroutines:${Versions.Libs.scarlet}")
        object ScarletAdapterProtobuf: Libs("com.tinder.scarlet:message-adapter-protobuf:${Versions.Libs.scarlet}")
        object ScarletAdapterGson: Libs("com.tinder.scarlet:message-adapter-gson:${Versions.Libs.scarlet}")
        object JsonKotlinSchema: Libs("net.pwall.json:json-kotlin-schema:${Versions.Libs.jsonKotlinSchema}")

        //protobuf
        object Protoc: Libs("com.google.protobuf:protoc:${Versions.Libs.protobuf}")
        object ProtobufJava: Libs("com.google.protobuf:protobuf-java:${Versions.Libs.protobuf}")
        object ProtobufKotlin: Libs("com.google.protobuf:protobuf-kotlin:${Versions.Libs.protobuf}")
        object ProtobufJavalite: Libs("com.google.protobuf:protobuf-javalite:${Versions.Libs.protobuf}")
        object ProtobufLite: Libs("com.google.protobuf:protobuf-lite:3.0.0")

        //jetpack
        object Annotation : Libs("androidx.annotation:annotation:${Versions.Libs.annotation}")
        object Appcompat : Libs("androidx.appcompat:appcompat:${Versions.Libs.appcompat}")
        object CoreKtx : Libs("androidx.core:core-ktx:${Versions.Libs.coreKtx}")
        object FragmentKtx : Libs("androidx.fragment:fragment-ktx:${Versions.Libs.fragmentKtx}")
        object LifecycleViewModelKtx : Libs("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Libs.lifecycle}")
        object LifecycleLiveDataKtx : Libs("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Libs.lifecycle}")
        object LifecycleRuntimeKtx : Libs("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Libs.lifecycle}")
        object LifecycleCommon : Libs("androidx.lifecycle:lifecycle-common-java8:${Versions.Libs.lifecycle}")
        object Transition : Libs("androidx.transition:transition:${Versions.Libs.transition}")
        object Interpolator : Libs("androidx.interpolator:interpolator:${Versions.Libs.interpolator}")
        object Material : Libs("com.google.android.material:material:${Versions.Libs.material}")
        object RecyclerView : Libs("androidx.recyclerview:recyclerview:${Versions.Libs.recyclerView}")
        object ConstraintLayout : Libs("androidx.constraintlayout:constraintlayout:${Versions.Libs.constraintLayout}")
        object CoordinatorLayout : Libs("androidx.coordinatorlayout:coordinatorlayout:${Versions.Libs.coordinatorLayout}")
        object ViewPager2 : Libs("androidx.viewpager2:viewpager2:${Versions.Libs.viewPager2}")

        //collection
        object ApacheCollections : Libs("org.apache.commons:commons-collections4:${Versions.Libs.apacheCollections}")

        //Compose Util
        object ComposeUtil : Libs("androidx.compose.ui:ui-util:${Versions.Libs.compose}")

        //Compose UI
        object ComposeUi : Libs("androidx.compose.ui:ui:${Versions.Libs.compose}")

        //Compose UI Tooling
        object ComposeUiTooling : Libs("androidx.compose.ui:ui-tooling:${Versions.Libs.compose}")

        //Compose Foundation
        object ComposeFoundation : Libs("androidx.compose.foundation:foundation:${Versions.Libs.compose}")

        //Compose Material
        object ComposeMaterial : Libs("androidx.compose.material:material:${Versions.Libs.compose}")

        //Compose Runtime LiveData
        object ComposeRuntimeLiveData : Libs("androidx.compose.runtime:runtime-livedata:${Versions.Libs.compose}")

        //Compose
//        sealed class LibSet(vararg libs: Libs) : Dependencies("") {
//            val libs: List<Libs> = listOf(*libs)
//
//            object Compose : LibSet(
//                ComposeUtil,
//                ComposeUi,
//                ComposeUiTooling,
//                ComposeFoundation,
//                ComposeMaterial,
//                ComposeRuntimeLiveData
//            )
//        }

        //testing
        object Junit : Libs("junit:junit:${Versions.Libs.junit}")
        object MockitoCore : Libs("org.mockito:mockito-core:${Versions.Libs.mockito}")
        object CoroutinesTest : Libs("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Libs.coroutines}")
        object TestCore : Libs("androidx.test:core:${Versions.Libs.testCore}")
        object TestRunner : Libs("androidx.test:runner:${Versions.Libs.testCore}")
        object TestRules : Libs("androidx.test:rules:${Versions.Libs.testCore}")

        //Assertions
        object TestExtJunit : Libs("androidx.test.ext:junit:${Versions.Libs.testExtJunit}")
        object TestExtTruth : Libs("androidx.test.ext:truth:${Versions.Libs.testExtTruth}")

        //espresso
        object EspressoCore : Libs("androidx.test.espresso:espresso-core:${Versions.Libs.espresso}")
        object EspressoContrib : Libs("androidx.test.espresso:espresso-contrib:${Versions.Libs.espresso}")
        object EspressoIntents : Libs("androidx.test.espresso:espresso-intents:${Versions.Libs.espresso}")
        object EspressoAccessibility : Libs("androidx.test.espresso:espresso-accessibility:${Versions.Libs.espresso}")
        object EspressoWeb : Libs("androidx.test.espresso:espresso-web:${Versions.Libs.espresso}")
        object EspressoIdling : Libs("androidx.test.espresso.idling:idling-concurrent:${Versions.Libs.espresso}")
        object EspressoPageObject : Libs("com.atiurin.espresso:espressopageobject:${Versions.Libs.espressoPageObject}")
    }
}
