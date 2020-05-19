import com.discoveryone.buildsrc.Dependencies

plugins {
    id("kotlin")
    id("com.discoveryone.buildsrc.coverage")
    id("com.discoveryone.buildsrc.deploy")
}

dependencies {
    implementation(Dependencies.kotlinStdLib)

    testImplementation(Dependencies.junit)
}
