import com.discoveryone.buildsrc.AndroidConfig
import com.discoveryone.buildsrc.Dependencies

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(AndroidConfig.targetSdkVersion)
}

dependencies {

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.activity)
    implementation(Dependencies.fragment)
    implementation(Dependencies.testCore)
    implementation(Dependencies.espressoCore)
}