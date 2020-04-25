package com.discoveryone.buildsrc

import com.android.build.gradle.BaseExtension

plugins {
    id("jacoco")
}

if (extensions.findByType(BaseExtension::class.java) != null) {
    android {
        buildTypes {
            getByName("debug") {
                isTestCoverageEnabled = true
            }
        }
        registerCoverageTaskForAndroidProject()
        fixRobolectricCoverage()
    }
} else {
    registerCoverageTaskForJvmProject()
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

fun Project.registerCoverageTaskForJvmProject() {
    val unitTestTask = "test"
    registerCoverageTask(
        jacocoReportTaskName = "generateJacocoReport",
        classpath = "${project.buildDir}/classes/kotlin",
        dependencies = listOf(unitTestTask),
        jacocoCoverageExecList = listOf("jacoco/$unitTestTask.exec")
    )
}

fun Project.registerCoverageTaskForAndroidProject() {
    val unitTestTask = "testDebugUnitTest"
    registerCoverageTask(
        jacocoReportTaskName = "generateJacocoReport",
        classpath = "${project.buildDir}/tmp/kotlin-classes/debug",
        dependencies = listOf(unitTestTask, "createDebugCoverageReport"),
        jacocoCoverageExecList = listOf(
            "jacoco/$unitTestTask.exec",
            "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
        )
    )
}

fun Project.registerCoverageTask(
    jacocoReportTaskName: String,
    classpath: String,
    dependencies: List<String>,
    jacocoCoverageExecList: List<String>
) {
    tasks.register(jacocoReportTaskName, JacocoReport::class) {
        description = "Generates a jacoco report task for the project"
        group = "verification"
        setDependsOn(dependencies)
        reports {
            xml.isEnabled = true
            // helps CI to auto-discover the coverage report
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
            mapOf("dir" to buildDir, "includes" to jacocoCoverageExecList)
        )

        sourceDirectories.setFrom(sources)
        classDirectories.setFrom(classes)
        executionData.setFrom(jacocoCoverageExec)
    }
}

fun Project.android(configure: BaseExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("android", configure)
