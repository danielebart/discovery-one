import com.discoveryone.buildsrc.AndroidConfig
import com.discoveryone.buildsrc.Dependencies

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(AndroidConfig.targetSdkVersion)

    defaultConfig {
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        testInstrumentationRunner = AndroidConfig.testInstrumentationRunner
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(project(":destination"))

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.lifecycleExtensions)
    implementation(Dependencies.fragment)
    implementation(Dependencies.activity)

    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.testCore)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.espressoCore)
    testImplementation(Dependencies.espressoIntents)
    testImplementation(Dependencies.fragmentTesting)

    testImplementation(project(":testutils"))
}