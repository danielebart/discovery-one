package com.discoveryone.buildsrc

object Dependencies {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"
    const val autocommon = "com.google.auto:auto-common:${Versions.autocommon}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    // testing
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val testCore = "androidx.test:core:${Versions.testCore}"
    const val junit = "junit:junit:${Versions.junit}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragmentTesting}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val kotlinCompileTesting =
        "com.github.tschuchortdev:kotlin-compile-testing:${Versions.kotlinCompileTesting}"
}
