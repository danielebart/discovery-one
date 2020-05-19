package com.discoveryone.processor.routes

import com.discoveryone.processor.assertGeneratedFile
import com.discoveryone.processor.createKotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Test

class DialogFragmentRouteGenerationTest {

    @Test
    fun `GIVEN a class annotated with @DialogFragmentRoute without args WHEN compiling using RouteProcessor THEN a DialogFragmentRoute is generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.DialogFragmentRoute
            import com.discoveryone.processor.fakes.DialogFragment
        
            @DialogFragmentRoute(name = "FAKE_ROUTE")
            class FakeDialogFragment: DialogFragment()
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

            import com.discoveryone.routes.GeneratedDialogFragmentRoute
            import kotlin.reflect.KClass
            
            object FAKE_ROUTE : GeneratedDialogFragmentRoute {
              override val clazz: KClass<*> = FakeDialogFragment::class
            }
            
            """
        )
    }

    @Test
    fun `GIVEN a class annotated with @DialogFragmentRoute with args WHEN compiling using RouteProcessor THEN an DialogFragmentRoute is generated with args`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage

            import com.discoveryone.annotations.DialogFragmentRoute
            import com.discoveryone.annotations.RouteArgument
            import com.discoveryone.processor.fakes.DialogFragment

            @DialogFragmentRoute(
                "FAKE_ROUTE", 
                arguments = [
                    RouteArgument("arg1", String::class), 
                    RouteArgument("arg2", Int::class), 
                    RouteArgument("arg3", Double::class)
                ]
            )
            class FakeDialogFragment: DialogFragment()
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

            import com.discoveryone.routes.GeneratedDialogFragmentRoute
            import kotlin.Double
            import kotlin.Int
            import kotlin.String
            import kotlin.reflect.KClass

            data class FAKE_ROUTE(
              val arg1: String,
              val arg2: Int,
              val arg3: Double
            ) : GeneratedDialogFragmentRoute {
              override val clazz: KClass<*> = FakeDialogFragment::class
            }

            """
        )
    }

    @Test
    fun `GIVEN a class annotated with @DialogFragmentRoute without name WHEN compiling using RouteProcessor THEN a DialogFragmentRoute is generated with name containing original class name`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.DialogFragmentRoute
        
            @DialogFragmentRoute
            class FakeDialogFragment
            """
        )
        val kotlinCompilation =
            createKotlinCompilation(kotlinSource)

        val result = kotlinCompilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        kotlinCompilation.assertGeneratedFile(
            "fakepackage/FakeDialogFragmentRoute.kt",
            """
            package fakepackage

            import com.discoveryone.routes.GeneratedDialogFragmentRoute
            import kotlin.reflect.KClass
            
            object FakeDialogFragmentRoute : GeneratedDialogFragmentRoute {
              override val clazz: KClass<*> = FakeDialogFragment::class
            }
            
            """
        )
    }
}