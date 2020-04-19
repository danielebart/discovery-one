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

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.kotlinCompileTesting)
}
