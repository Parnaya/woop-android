import Dependencies.*

plugins {
    androidLibrary()
    kotlinAndroid()
    modulePlugin()
}

android.buildFeatures.viewBinding = true

dependencies {
    //Dev
    implementation(
        Libs.Appcompat,
        Libs.ConstraintLayout,
        Libs.CoreKtx,
        Libs.Coroutines,
        Libs.FragmentKtx,
        Libs.Koin,
        Libs.Kotlin,
        Libs.LifecycleCommon,
        Libs.LifecycleLiveDataKtx,
        Libs.LifecycleRuntimeKtx,
        Libs.LifecycleViewModelKtx,
        Libs.Material,
        Libs.Transition,
    )
    //Unit testing
    testImplementation(
        Libs.CoroutinesTest,
        Libs.Junit,
        Libs.MockitoCore,
        Libs.TestCore,
    )
}
