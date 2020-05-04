package com.discoveryone.processor

import com.discoveryone.routes.GeneratedFragmentRoute
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class FragmentRouteGenerationTest {

    @Test
    fun `GIVEN a class annotated with @FragmentRoute without args WHEN compiling using RouteProcessor THEN a FragmentRoute is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage
        
        import com.discoveryone.annotations.FragmentRoute
        
        @FragmentRoute(name = "FAKE_ROUTE", containerId = 439)
        class FakeFragment
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(RouteProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAFragmentRoute(
            expectedRouteName = "fakepackage.FAKE_ROUTE",
            expectedClassArgQualifiedName = "fakepackage.FakeFragment",
            expectedContainerId = 439
        )
    }

    @Test
    fun `GIVEN a class annotated with @FragmentRoute with args WHEN compiling using RouteProcessor THEN an FragmentRoute is generated with args`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage

        import com.discoveryone.annotations.FragmentRoute
        import com.discoveryone.annotations.RouteArgument

        @FragmentRoute(
            "FAKE_ROUTE", 
            containerId = 789, 
            arguments = [
                RouteArgument("arg1", String::class), 
                RouteArgument("arg2", Int::class), 
                RouteArgument("arg3", Double::class)
                ]
        )
        class FakeFragment
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(RouteProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAFragmentRouteWithArgs(
            expectedRouteName = "fakepackage.FAKE_ROUTE",
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
    fun `GIVEN a class annotated with @FragmentRoute without name WHEN compiling using RouteProcessor THEN a FragmentRoute is generated with name containing original class name`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage

        import com.discoveryone.annotations.FragmentRoute

        @FragmentRoute(containerId = 789)
        class FakeFragment
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(RouteProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAFragmentRoute(
            expectedRouteName = "fakepackage.FakeFragmentRoute",
            expectedClassArgQualifiedName = "fakepackage.FakeFragment",
            expectedContainerId = 789
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAFragmentRoute(
        expectedRouteName: String,
        expectedClassArgQualifiedName: String,
        expectedContainerId: Int
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(expectedRouteName)
        val instance = generatedClass.getField("INSTANCE").get(null)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        val containerIdValue = generatedClass.getMethod("getContainerId").invoke(instance) as Int
        assertEquals(expectedClassArgQualifiedName, classValue.qualifiedName)
        assertEquals(expectedContainerId, containerIdValue)
        assertEquals(
            GeneratedFragmentRoute::class.qualifiedName,
            generatedClass.interfaces.first().name
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAFragmentRouteWithArgs(
        expectedRouteName: String,
        expectedClassArgQualifiedName: String,
        expectedContainerId: Int,
        expectedArgs: List<Pair<String, KClass<*>>> = emptyList()
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(expectedRouteName)
        val instance = generatedClass.constructors.first().newInstance("arg1", 30, 50.0)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        val containerIdValue = generatedClass.getMethod("getContainerId").invoke(instance) as Int
        assertEquals(expectedClassArgQualifiedName, classValue.qualifiedName)
        assertEquals(expectedContainerId, containerIdValue)
        assertEquals(
            GeneratedFragmentRoute::class.qualifiedName,
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