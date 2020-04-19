package com.discoveryone.processor

import com.discoveryone.destination.FragmentDestination
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class FragmentDestinationGenerationTest {

    @Test
    fun `GIVEN a class annotated with @FragmentNavigationDestination without args WHEN compiling using DestinationProcessor THEN a FragmentDestination is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage
        
        import com.discoveryone.destination.FragmentNavigationDestination
        
        @FragmentNavigationDestination(name = "FAKE_DESTINATION", containerId = 439)
        class FakeFragment
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(DestinationProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAFragmentDestination(
            expectedDestinationName = "fakepackage.FAKE_DESTINATION",
            expectedClassArgQualifiedName = "fakepackage.FakeFragment",
            expectedContainerId = 439
        )
    }

    @Test
    fun `GIVEN a class annotated with @FragmentNavigationDestination with args WHEN compiling using DestinationProcessor THEN an FragmentDestination is generated with args`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage

        import com.discoveryone.destination.FragmentNavigationDestination
        import com.discoveryone.destination.DestinationArgument

        @FragmentNavigationDestination(
            "FAKE_DESTINATION", 
            containerId = 789, 
            arguments = [
                DestinationArgument("arg1", String::class), 
                DestinationArgument("arg2", Int::class), 
                DestinationArgument("arg3", Double::class)
                ]
        )
        class FakeFragment
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(DestinationProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAFragmentDestinationWithArgs(
            expectedDestinationName = "fakepackage.FAKE_DESTINATION",
            expectedClassArgQualifiedName = "fakepackage.FakeFragment",
            expectedContainerId = 789,
            expectedArgs = listOf(
                Pair("arg1", String::class),
                Pair("arg2", Int::class),
                Pair("arg3", Double::class)
            )
        )
    }

    @Test
    fun `GIVEN a class annotated with @FragmentNavigationDestination without name WHEN compiling using DestinationProcessor THEN a FragmentDestination is generated with name containing original class name`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage

        import com.discoveryone.destination.FragmentNavigationDestination

        @FragmentNavigationDestination(containerId = 789)
        class FakeFragment
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(DestinationProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAFragmentDestination(
            expectedDestinationName = "fakepackage.FakeFragmentDestination",
            expectedClassArgQualifiedName = "fakepackage.FakeFragment",
            expectedContainerId = 789
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAFragmentDestination(
        expectedDestinationName: String,
        expectedClassArgQualifiedName: String,
        expectedContainerId: Int
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(expectedDestinationName)
        val instance = generatedClass.getField("INSTANCE").get(null)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        val containerIdValue = generatedClass.getMethod("getContainerId").invoke(instance) as Int
        assertEquals(expectedClassArgQualifiedName, classValue.qualifiedName)
        assertEquals(expectedContainerId, containerIdValue)
        assertEquals(
            FragmentDestination::class.qualifiedName,
            generatedClass.interfaces.first().name
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAFragmentDestinationWithArgs(
        expectedDestinationName: String,
        expectedClassArgQualifiedName: String,
        expectedContainerId: Int,
        expectedArgs: List<Pair<String, KClass<*>>> = emptyList()
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(expectedDestinationName)
        val instance = generatedClass.constructors.first().newInstance("arg1", 30, 50.0)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        val containerIdValue = generatedClass.getMethod("getContainerId").invoke(instance) as Int
        assertEquals(expectedClassArgQualifiedName, classValue.qualifiedName)
        assertEquals(expectedContainerId, containerIdValue)
        assertEquals(
            FragmentDestination::class.qualifiedName,
            generatedClass.interfaces.first().name
        )
        expectedArgs.forEach {
            val (argName, argClass) = it
            assertTrue(instance::class.isData)
            assertTrue(
                instance::class.memberProperties.any { property ->
                    property.hasNameAndType(
                        argName = argName,
                        argClass = argClass,
                        instance = instance
                    )
                }
            )
        }
    }

    private fun KProperty1<*, *>.hasNameAndType(
        argName: String,
        argClass: KClass<*>,
        instance: Any
    ): Boolean =
        name == argName && getter.call(instance)!!::class.qualifiedName == argClass.qualifiedName
}