import com.discoveryone.buildsrc.AndroidConfig
import com.discoveryone.buildsrc.Dependencies
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

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
    implementation(project(":discoveryone"))

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.appCompat)
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    implementation("androidx.appcompat:appcompat:1.1.0")

    kapt(project(":processor"))
}