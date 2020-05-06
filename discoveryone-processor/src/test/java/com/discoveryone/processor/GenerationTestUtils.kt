package com.discoveryone.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import java.io.File
import java.io.FileReader

fun KotlinCompilation.assertGeneratedFile(
    filePath: String,
    @Language("kotlin") content: String
) {
    assertEquals(
        content.trimIndent(),
        FileReader("${workingDir.absolutePath}/kapt/sources/$filePath").readText()
    )
}

fun KotlinCompilation.assertFileNotGenerated(
    filePath: String
) {
    assertFalse(File("${workingDir.absolutePath}/kapt/sources/$filePath").exists())
}

fun createKotlinCompilation(vararg sources: SourceFile): KotlinCompilation =
    KotlinCompilation().apply {
        this.sources = sources.toList()
        annotationProcessors = listOf(DiscoveryOneProcessor())
        inheritClassPath = true
    }