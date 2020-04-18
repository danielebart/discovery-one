import com.discoveryone.buildsrc.AndroidConfig
import com.discoveryone.buildsrc.Dependencies
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

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

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        all(KotlinClosure1<Test, Test>({
            apply {
                testLogging.exceptionFormat = TestExceptionFormat.FULL
                testLogging.events = setOf(
                    TestLogEvent.SKIPPED,
                    TestLogEvent.PASSED,
                    TestLogEvent.FAILED
                )
            }
        }, this))
    }

    // fixes robolectric coverage calculation
    tasks.withType(Test::class.java)
        .mapNotNull { it.extensions.findByType(JacocoTaskExtension::class.java) }
        .forEach { it.isIncludeNoLocationClasses = true }
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