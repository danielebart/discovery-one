import com.discoveryone.buildsrc.Dependencies

plugins {
    id("kotlin")
    id("com.discoveryone.buildsrc.coverage")
}

dependencies {
    implementation(Dependencies.kotlinStdLib)

    testImplementation(Dependencies.junit)
}
