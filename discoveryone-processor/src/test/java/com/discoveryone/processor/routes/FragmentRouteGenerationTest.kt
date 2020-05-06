package com.discoveryone.processor.routes

import com.discoveryone.processor.assertGeneratedFile
import com.discoveryone.processor.createKotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Test

class FragmentRouteGenerationTest {

    @Test
    fun `GIVEN a class annotated with @FragmentRoute without args WHEN compiling using RouteProcessor THEN a FragmentRoute is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.FragmentRoute
        
            @FragmentRoute(name = "FAKE_ROUTE", containerId = 439)
            class FakeFragment
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

            import com.discoveryone.routes.GeneratedFragmentRoute
            import kotlin.Int
            import kotlin.reflect.KClass
            
            object FAKE_ROUTE : GeneratedFragmentRoute {
              override val clazz: KClass<*> = FakeFragment::class

              override val containerId: Int = 439
            }
            
            """
        )
    }

    @Test
    fun `GIVEN a class annotated with @FragmentRoute with args WHEN compiling using RouteProcessor THEN an FragmentRoute is generated with args`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage

            import com.discoveryone.annotations.FragmentRoute
            import com.discoveryone.annotations.RouteArgument
            import com.discoveryone.processor.fakes.Fragment

            @FragmentRoute(
                "FAKE_ROUTE", 
                containerId = 789, 
                arguments = [
                    RouteArgument("arg1", String::class), 
                    RouteArgument("arg2", Int::class), 
                    RouteArgument("arg3", Double::class)
                ]
            )
            class FakeFragment: Fragment()
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
            import com.discoveryone.routes.GeneratedFragmentRoute
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
            ) : GeneratedFragmentRoute {
              override val clazz: KClass<*> = FakeFragment::class

              override val containerId: Int = 789
            }

            """
        )
    }

    @Test
    fun `GIVEN a class annotated with @FragmentRoute without name WHEN compiling using RouteProcessor THEN a FragmentRoute is generated with name containing original class name`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.FragmentRoute
        
            @FragmentRoute(containerId = 439)
            class FakeFragment
            """
        )
        val kotlinCompilation =
            createKotlinCompilation(kotlinSource)

        val result = kotlinCompilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        kotlinCompilation.assertGeneratedFile(
            "fakepackage/FakeFragmentRoute.kt",
            """
            package fakepackage

            import com.discoveryone.routes.GeneratedFragmentRoute
            import kotlin.Int
            import kotlin.reflect.KClass
            
            object FakeFragmentRoute : GeneratedFragmentRoute {
              override val clazz: KClass<*> = FakeFragment::class

              override val containerId: Int = 439
            }
            
            """
        )
    }
}