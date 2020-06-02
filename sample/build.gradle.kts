import com.discoveryone.buildsrc.AndroidConfig
import com.discoveryone.buildsrc.Dependencies

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(AndroidConfig.targetSdkVersion)
    defaultConfig {
        applicationId = AndroidConfig.applicationId
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        versionCode = 1
        versionName = "0.1"
        testInstrumentationRunner = AndroidConfig.testInstrumentationRunner
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    val discoveryOneVersion = "0.11.0"

    implementation("com.github.danielebart:discoveryone-runtime:$discoveryOneVersion")

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.activity)
    implementation(Dependencies.fragment)

    kapt("com.github.danielebart:discoveryone-compiler:$discoveryOneVersion")
}