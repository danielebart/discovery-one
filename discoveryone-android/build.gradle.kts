import com.discoveryone.buildsrc.AndroidConfig
import com.discoveryone.buildsrc.Dependencies
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.discoveryone.buildsrc.coverage")
    id("kotlin-kapt")
    id("com.discoveryone.buildsrc.deploy")
}

deployModule {
    artifactId = "runtime"
    publication = "release"
}

android {
    compileSdkVersion(AndroidConfig.targetSdkVersion)

    defaultConfig {
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        testInstrumentationRunner = AndroidConfig.testInstrumentationRunner
    }

    buildTypes {
        getByName("release") {
            consumerProguardFile("proguard-rules.pro")
        }
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

    lintOptions {
        isWarningsAsErrors = true
    }
}

dependencies {
    api(project(":discoveryone-core"))

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.lifecycleExtensions)
    implementation(Dependencies.fragment)
    implementation(Dependencies.activity)

    debugImplementation(Dependencies.leakCanary)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.coreKtx)

    androidTestImplementation(Dependencies.testCore)
    androidTestImplementation(Dependencies.espressoCore)
    androidTestImplementation(Dependencies.espressoIntents)
    androidTestImplementation(Dependencies.fragmentTesting)

    kaptAndroidTest(project(":discoveryone-processor"))
}