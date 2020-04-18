package com.discoveryone.buildsrc

import com.android.build.gradle.BaseExtension

plugins {
    id("jacoco")
}

if (extensions.findByType(BaseExtension::class.java) != null) {
    android {
        buildTypes.forEach { project.registerCoverageTask(it.name) }
        fixRobolectricCoverage()
    }
} else {
    project.registerCoverageTask()
}

fun BaseExtension.fixRobolectricCoverage() {
    testOptions.unitTests.all(
        KotlinClosure1<Test, Test>({
            extensions.configure(JacocoTaskExtension::class.java) {
                isIncludeNoLocationClasses = true
            }
            this
        }, this)
    )
}

fun Project.registerCoverageTask(buildType: String? = null) {
    val (unitTestTask, jacocoReportTaskName, classpath) = if (buildType != null) {
        Triple(
            "test${buildType.capitalize()}UnitTest",
            "${buildType}JacocoReport",
            "${project.buildDir}/tmp/kotlin-classes/$buildType"
        )
    } else {
        Triple("test", "jacocoReport", "${project.buildDir}/classes/kotlin")
    }

    tasks.register(jacocoReportTaskName, JacocoReport::class) {
        description = "Generates a jacoco report task for the project"
        group = "verification"
        setDependsOn(listOf(unitTestTask))
        reports {
            xml.isEnabled = true
            // helps CI to auto discovery the coverage report
            xml.destination = File("${project.buildDir}/jacoco/coverage.xml")
        }

        val classes = fileTree(
            mapOf(
                "dir" to classpath,
                "excludes" to listOf<String>()
            )
        )
        val sources = "${project.projectDir}/src/main/java"
        val jacocoCoverageExec = fileTree(
            mapOf(
                "dir" to buildDir, "includes" to listOf("jacoco/$unitTestTask.exec")
            )
        )

        sourceDirectories.setFrom(sources)
        classDirectories.setFrom(classes)
        executionData.setFrom(jacocoCoverageExec)
    }
}

fun Project.android(configure: BaseExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("android", configure)