import com.sailor.buildsrc.Dependencies

plugins {
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":destination"))

    implementation(Dependencies.kotlinPoet)
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.autocommon)
}
