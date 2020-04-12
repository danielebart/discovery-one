plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

apply {
    from("src/main/kotlin/com/discoveryone/buildsrc/extract-build-deps.gradle.kts")
}

val agp: String by project.extra
val kotlin: String by project.extra

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:$agp")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
}