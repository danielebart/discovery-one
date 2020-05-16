package com.discoveryone.buildsrc

import com.android.build.gradle.BaseExtension
import java.util.Properties

plugins {
    id("com.jfrog.bintray")
    id("maven-publish")
}

version = "0.1.0"

val deployProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

bintray {
    user = deployProperties.getProperty("bintray.user")
    key = deployProperties.getProperty("bintray.apikey")

    setPublications("release")

    pkg.apply {
        repo = "DiscoveryOne"
        name = "org.discovery1"
        vcsUrl = "https://github.com/danielebart/discovery-one"
        setLicenses("MIT")
        publish = true
    }
}


gradle.projectsEvaluated {
    val sourceSets = rootProject.subprojects.map { subproject ->
        val androidExtensions = subproject.extensions.findByName("android") as? BaseExtension
        if (subproject.extensions.findByName("android") != null) {
            androidExtensions!!.sourceSets["main"].java.srcDirs
        } else {
            subproject.convention.getPlugin(JavaPluginConvention::class)
                .sourceSets["main"]
                .java
                .srcDirs
        }
    }.reduce { accumulator, currentSet -> accumulator + currentSet }

    val sourceJar by tasks.registering(Jar::class) {
        from(sourceSets)
    }

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "org.discovery1"
                artifactId = "discoveryone-core"
                version = "0.1.0"
                artifact(sourceJar.get())
            }
        }
    }
}
