package com.discoveryone.core

import com.discoveryone.NavigationHandler
import com.discoveryone.Navigator
import com.discoveryone.ResultToken
import com.discoveryone.destinations.AbstractDestination
import com.discoveryone.registerResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class NavigatorTest {

    @Before
    fun setup() {
        Navigator.initialize(FakeNavigationHandler)
    }

    @Test
    fun `WHEN navigating to 3 different destinations THEN handler is called 3 times`() {
        Navigator.navigate(FakeDestination1)
        Navigator.navigate(FakeDestination2)
        Navigator.navigate(FakeDestination3)

        assertEquals(
            listOf(FakeDestination1, FakeDestination2, FakeDestination3),
            FakeNavigationHandler.destinationHistorySpy
        )
    }

    @Test
    fun `WHEN navigating to a destination for result given a token THEN handler is called for result with same token`() {
        Navigator.navigateForResult<String>(FakeDestination1, ResultToken(345))

        assertEquals(
            listOf(Pair(FakeDestination1, ResultToken(345))),
            FakeNavigationHandler.destinationForResultHistorySpy
        )
    }

    @Test
    fun `WHEN registering 3 times for a result THEN handler is called for registering a result 3 times`() {
        Navigator.registerResult<Int> {  }
        Navigator.registerResult<Int> {  }
        Navigator.registerResult<Int> {  }

        assertEquals(
            3,
            FakeNavigationHandler.registerResultSpyCounter
        )
    }

    object FakeNavigationHandler : NavigationHandler {

        var destinationHistorySpy: MutableList<AbstractDestination> = mutableListOf()
        val destinationForResultHistorySpy: MutableList<Pair<AbstractDestination, ResultToken>> = mutableListOf()
        var registerResultSpyCounter = 0

        override fun navigate(destination: AbstractDestination) {
            destinationHistorySpy.add(destination)
        }

        override fun <T> navigateForResult(destination: AbstractDestination, token: ResultToken) {
            destinationForResultHistorySpy.add(Pair(destination, token))
        }

        override fun <T : Any> registerResult(
            resultClass: KClass<T>,
            action: (T) -> Unit
        ): ResultToken {
            registerResultSpyCounter++
            return ResultToken(0)
        }
    }

    object FakeDestination1 : AbstractDestination {
        override val clazz: KClass<*> = Any::class
    }

    object FakeDestination2 : AbstractDestination {
        override val clazz: KClass<*> = Any::class
    }

    object FakeDestination3 : AbstractDestination {
        override val clazz: KClass<*> = Any::class
    }
}