package com.discoveryone.processor.safeargs

import com.discoveryone.processor.assertFileNotGenerated
import com.discoveryone.processor.assertGeneratedFile
import com.discoveryone.processor.createKotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert.assertEquals
import org.junit.Test

class ActivitySafeArgsGenerationTest {

    @Test
    fun `GIVEN a class annotated with @ActivityRoute without args WHEN compiling using RouteProcessor THEN no safe args extensions should be generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage
        
            import com.discoveryone.annotations.ActivityRoute
        
            @ActivityRoute(name = "FakeRoute")
            class FakeActivity
            """
        )
        val kotlinCompilation =
            createKotlinCompilation(kotlinSource)

        val result = kotlinCompilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        kotlinCompilation.assertFileNotGenerated("fakepackage/FakeRouteExtensions.kt")
    }

    @Test
    fun `GIVEN a class annotated with @ActivityRoute with args WHEN compiling using RouteProcessor THEN safe args extensions are generated`() {
        val kotlinSource = SourceFile.kotlin(
            "fakeClass.kt",
            """
            package fakepackage

            import com.discoveryone.annotations.ActivityRoute
            import com.discoveryone.annotations.RouteArgument
            import com.discoveryone.processor.fakes.Activity

            @ActivityRoute(
                "FakeRoute", 
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
            "fakepackage/FakeActivityExtensions.kt",
            """
            package fakepackage

            import kotlin.Double
            import kotlin.Int
            import kotlin.String
            
            val FakeActivity.arg1: String
              get() = intent.extras!!.get("arg1") as String
            
            val FakeActivity.arg2: Int
              get() = intent.extras!!.get("arg2") as Int
            
            val FakeActivity.arg3: Double
              get() = intent.extras!!.get("arg3") as Double

            """
        )
    }
}