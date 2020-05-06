package com.discoveryone.processor.safeargs

import com.discoveryone.processor.assertFileNotGenerated
import com.discoveryone.processor.assertGeneratedFile
import com.discoveryone.processor.createKotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Test

class FragmentSafeArgsGenerationTest {

    @Test
    fun `GIVEN a class annotated with @FragmentRoute without args WHEN compiling using RouteProcessor THEN no safe args extensions should be generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.FragmentRoute
        
            @FragmentRoute(name = "FakeRoute", containerId = 439)
            class FakeFragment
            """
        )
        val kotlinCompilation =
            createKotlinCompilation(kotlinSource)

        val result = kotlinCompilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        kotlinCompilation.assertFileNotGenerated("fakepackage/FakeRouteExtensions.kt")
    }

    @Test
    fun `GIVEN a class annotated with @FragmentRoute with args WHEN compiling using RouteProcessor THEN safe args extensions are generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage

            import com.discoveryone.annotations.FragmentRoute
        import com.discoveryone.annotations.RouteArgument
        import com.discoveryone.processor.fakes.Fragment

            @FragmentRoute(
                "FakeRoute", 
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
            "fakepackage/FakeFragmentExtensions.kt",
            """
            package fakepackage

            import kotlin.Double
            import kotlin.Int
            import kotlin.String
            
            val FakeFragment.arg1: String
              get() = requireArguments().get("arg1") as String
            
            val FakeFragment.arg2: Int
              get() = requireArguments().get("arg2") as Int
            
            val FakeFragment.arg3: Double
              get() = requireArguments().get("arg3") as Double

            """
        )
    }
}