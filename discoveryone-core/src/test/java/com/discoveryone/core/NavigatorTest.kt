package com.discoveryone.core

import com.discoveryone.NavigationHandler
import com.discoveryone.Navigator
import com.discoveryone.destinations.AbstractDestination
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class NavigatorTest {

    @Before
    fun setup() {
        Navigator.setNavigationHandler(FakeNavigationHandler)
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

    object FakeNavigationHandler : NavigationHandler {

        var destinationHistorySpy: MutableList<AbstractDestination> = mutableListOf()

        override fun navigate(destination: AbstractDestination) {
            destinationHistorySpy.add(destination)
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