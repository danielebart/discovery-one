package com.discoveryone.buildsrc

import com.android.build.gradle.BaseExtension
import java.util.Properties

plugins {
    id("com.jfrog.bintray")
    id("maven-publish")
}

val deployVersion: String? by project
version = deployVersion ?: ""
val deployProperties = Properties().apply {
    load(rootProject.file("deploy.properties").inputStream())
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("release")
    pkg.apply {
        repo = deployProperties.getProperty("repo")
        name = deployProperties.getProperty("groupId")
        vcsUrl = deployProperties.getProperty("vcsUrl")
        setLicenses(deployProperties.getProperty("license"))
        publish = true
    }
}

gradle.projectsEvaluated {
    val sourceJar by tasks.registering(Jar::class) {
        from(extractSourceSetsFromSubprojects())
        archiveClassifier.set("sources")
    }

    publishing {
        publications {
            create<MavenPublication>("release") {
                val component = if (project.isAndroidProject()) "release" else "java"
                from(components[component])
                groupId = deployProperties.getProperty("groupId")
                artifactId = project.name
                version = deployVersion ?: ""
                artifact(sourceJar.get())

                pom {
                    name.set(deployProperties.getProperty("name"))
                    description.set(deployProperties.getProperty("description"))
                    licenses {
                        license {
                            name.set(deployProperties.getProperty("license"))
                            url.set(deployProperties.getProperty("licenseUrl"))
                        }
                    }
                    developers {
                        developer {
                            name.set(deployProperties.getProperty("maintainer"))
                            email.set(deployProperties.getProperty("email"))
                        }
                    }
                    scm {
                        url.set(deployProperties.getProperty("vcsUrl"))
                    }
                }
            }
        }
    }
}

fun extractSourceSetsFromSubprojects(): Collection<File> =
    rootProject.subprojects.map { subproject ->
        if (subproject.isAndroidProject()) {
            subproject.androidExtension.sourceSets["main"].java.srcDirs
        } else {
            subproject.convention.getPlugin(JavaPluginConvention::class)
                .sourceSets["main"]
                .java
                .srcDirs
        }
    }.flatten()

fun Project.isAndroidProject(): Boolean =
    extensions.findByName("android") is BaseExtension

val Project.androidExtension: BaseExtension
    get() = extensions.findByName("android") as BaseExtension