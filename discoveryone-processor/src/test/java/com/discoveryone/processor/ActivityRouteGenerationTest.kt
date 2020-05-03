package com.discoveryone.processor

import com.discoveryone.routes.GeneratedActivityRoute
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ActivityRouteGenerationTest {

    @Test
    fun `GIVEN a class annotated with @ActivityRoute without args WHEN compiling using RouteProcessor THEN an ActivityRoute is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage
        
        import com.discoveryone.annotations.ActivityRoute
        
        @ActivityRoute("FAKE_ROUTE")
        class FakeActivity
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(RouteProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAnActivityRoute(
            "fakepackage.FAKE_ROUTE",
            "fakepackage.FakeActivity"
        )
    }

    @Test
    fun `GIVEN a class annotated with @ActivityRoute with args WHEN compiling using RouteProcessor THEN an ActivityRoute is generated with args`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage
        
        import com.discoveryone.annotations.ActivityRoute
        import com.discoveryone.annotations.RouteArgument
        
        @ActivityRoute(
            "FAKE_ROUTE", 
            arguments = [
                RouteArgument("arg1", String::class), 
                RouteArgument("arg2", Int::class), 
                RouteArgument("arg3", Double::class)
                ]
        )
        class FakeActivity
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(RouteProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAnActivityRouteWithArgs(
            "fakepackage.FAKE_ROUTE",
            "fakepackage.FakeActivity",
            listOf(
                Pair("arg1", String::class),
                Pair("arg2", Int::class),
                Pair("arg3", Double::class)
            )
        )
    }

    @Test
    fun `GIVEN a class annotated with @ActivityRoute without name WHEN compiling using RouteProcessor THEN an ActivityRoute is generated with name containing original class name`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage
        
        import com.discoveryone.annotations.ActivityRoute
        
        @ActivityRoute
        class FakeActivity
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(RouteProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAnActivityRoute(
            "fakepackage.FakeActivityRoute",
            "fakepackage.FakeActivity"
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAnActivityRoute(
        routeName: String,
        originalActivityName: String
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(routeName)
        val instance = generatedClass.getField("INSTANCE").get(null)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        assertEquals(originalActivityName, classValue.qualifiedName)
        assertEquals(
            GeneratedActivityRoute::class.qualifiedName,
            generatedClass.interfaces.first().name
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAnActivityRouteWithArgs(
        routeName: String,
        originalActivityName: String,
        args: List<Pair<String, KClass<*>>> = emptyList()
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(routeName)
        val instance = generatedClass.constructors.first().newInstance("arg1", 30, 50.0)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        assertEquals(originalActivityName, classValue.qualifiedName)
        assertEquals(
            GeneratedActivityRoute::class.qualifiedName,
            generatedClass.interfaces.first().name
        )
        args.forEach {
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