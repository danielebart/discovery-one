import com.discoveryone.buildsrc.Dependencies

plugins {
    id("kotlin")
    id("kotlin-kapt")
    id("com.discoveryone.buildsrc.coverage")
}

dependencies {
    implementation(project(":discoveryone-core"))

    implementation(Dependencies.kotlinPoet)
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.autocommon)
    implementation(Dependencies.kotlinMetadata)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.kotlinCompileTesting)
}
