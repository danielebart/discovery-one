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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":discoveryone-android"))

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.activity)
    implementation(Dependencies.fragment)
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")

    kapt(project(":discoveryone-processor"))
}