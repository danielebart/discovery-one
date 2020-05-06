package com.discoveryone.processor.routes

import com.discoveryone.processor.assertGeneratedFile
import com.discoveryone.processor.createKotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Test

class ActivityRouteGenerationTest {

    @Test
    fun `GIVEN a class annotated with @ActivityRoute without args WHEN compiling using RouteProcessor THEN a ActivityRoute is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.ActivityRoute
            import com.discoveryone.processor.fakes.Activity
        
            @ActivityRoute(name = "FAKE_ROUTE")
            class FakeActivity: Activity()
            """
        )
        val kotlinCompilation =
            createKotlinCompilation(kotlinSource)

        val result = kotlinCompilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        kotlinCompilation.assertGeneratedFile(
            "fakepackage/FAKE_ROUTE.kt",
            """
            package fakepackage

            import com.discoveryone.routes.GeneratedActivityRoute
            import kotlin.reflect.KClass
            
            object FAKE_ROUTE : GeneratedActivityRoute {
              override val clazz: KClass<*> = FakeActivity::class
            }
            
            """
        )
    }

    @Test
    fun `GIVEN a class annotated with @ActivityRoute with args WHEN compiling using RouteProcessor THEN an ActivityRoute is generated with args`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage

            import com.discoveryone.annotations.ActivityRoute
            import com.discoveryone.annotations.RouteArgument
            import com.discoveryone.processor.fakes.Activity

            @ActivityRoute(
                "FAKE_ROUTE", 
                arguments = [
                    RouteArgument("arg1", String::class), 
                    RouteArgument("arg2", Int::class), 
                    RouteArgument("arg3", Double::class)
                ]
            )
            class FakeActivity: Activity()
            """
        )

        val kotlinCompilation =
            createKotlinCompilation(kotlinSource)

        val result = kotlinCompilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        kotlinCompilation.assertGeneratedFile(
            "fakepackage/FAKE_ROUTE.kt",
            """
            package fakepackage

            import com.discoveryone.annotations.InternalRouteArgumentMarker
            import com.discoveryone.routes.GeneratedActivityRoute
            import kotlin.Double
            import kotlin.Int
            import kotlin.String
            import kotlin.reflect.KClass

            data class FAKE_ROUTE(
              @InternalRouteArgumentMarker
              val arg1: String,
              @InternalRouteArgumentMarker
              val arg2: Int,
              @InternalRouteArgumentMarker
              val arg3: Double
            ) : GeneratedActivityRoute {
              override val clazz: KClass<*> = FakeActivity::class
            }

            """
        )
    }

    @Test
    fun `GIVEN a class annotated with @ActivityRoute without name WHEN compiling using RouteProcessor THEN a ActivityRoute is generated with name containing original class name`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.ActivityRoute
        
            @ActivityRoute
            class FakeActivity
            """
        )
        val kotlinCompilation =
            createKotlinCompilation(kotlinSource)

        val result = kotlinCompilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        kotlinCompilation.assertGeneratedFile(
            "fakepackage/FakeActivityRoute.kt",
            """
            package fakepackage

            import com.discoveryone.routes.GeneratedActivityRoute
            import kotlin.reflect.KClass
            
            object FakeActivityRoute : GeneratedActivityRoute {
              override val clazz: KClass<*> = FakeActivity::class
            }
            
            """
        )
    }
}