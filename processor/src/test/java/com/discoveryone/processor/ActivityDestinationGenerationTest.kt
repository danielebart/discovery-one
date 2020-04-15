package com.discoveryone.processor

import com.discoveryone.destination.ActivityDestination
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ActivityDestinationGenerationTest {

    @Test
    fun `GIVEN a class annotated with @ActivityNavigationDestination without args WHEN compiling using DestinationProcessor THEN an ActivityDestination is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage
        
        import com.discoveryone.destination.ActivityNavigationDestination
        
        @ActivityNavigationDestination("FAKE_DESTINATION")
        class FakeActivity
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(DestinationProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAnActivityDestination(
            "fakepackage.FAKE_DESTINATION",
            "fakepackage.FakeActivity"
        )
    }

    @Test
    fun `GIVEN a class annotated with @ActivityNavigationDestination with args WHEN compiling using DestinationProcessor THEN an ActivityDestination is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt", """
        package fakepackage
        
        import com.discoveryone.destination.ActivityNavigationDestination
        import com.discoveryone.destination.DestinationArgument
        
        @ActivityNavigationDestination("FAKE_DESTINATION", arguments = [DestinationArgument("arg1", String::class), DestinationArgument("arg2", Int::class), DestinationArgument("arg3", Double::class)])
        class FakeActivity
    """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            annotationProcessors = listOf(DestinationProcessor())
            inheritClassPath = true
        }.compile()

        result.assertGeneratedAnActivityDestinationWithArgs(
            "fakepackage.FAKE_DESTINATION",
            "fakepackage.FakeActivity",
            listOf(
                Pair("arg1", String::class),
                Pair("arg2", Int::class),
                Pair("arg3", Double::class)
            )
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAnActivityDestination(
        destinationName: String,
        originalActivityName: String
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(destinationName)
        val instance = generatedClass.getField("INSTANCE").get(null)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        assertEquals(originalActivityName, classValue.qualifiedName)
        assertEquals(
            ActivityDestination::class.qualifiedName,
            generatedClass.interfaces.first().name
        )
    }

    private fun KotlinCompilation.Result.assertGeneratedAnActivityDestinationWithArgs(
        destinationName: String,
        originalActivityName: String,
        args: List<Pair<String, KClass<*>>> = emptyList()
    ) {
        assertEquals(KotlinCompilation.ExitCode.OK, exitCode)
        val generatedClass = classLoader.loadClass(destinationName)
        val instance = generatedClass.constructors.first().newInstance("arg1", 30, 50.0)
        val classValue = generatedClass.getMethod("getClazz").invoke(instance) as KClass<*>
        assertEquals(originalActivityName, classValue.qualifiedName)
        assertEquals(
            ActivityDestination::class.qualifiedName,
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