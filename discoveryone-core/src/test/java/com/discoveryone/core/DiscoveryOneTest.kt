package com.discoveryone.core

import com.discoveryone.DiscoveryOne
import com.discoveryone.Navigator
import com.discoveryone.Scene
import com.discoveryone.routes.AbstractRoute
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class DiscoveryOneTest {

    @Before
    fun setup() {
        DiscoveryOne.install(FakeNavigator)
    }

    @After
    fun teardown() {
        FakeNavigator.clear()
    }

    @Test
    fun `WHEN navigating to 3 different routes THEN navigator is called 3 times`() {
        DiscoveryOne.navigator.navigate(StubScene(), FakeRoute1)
        DiscoveryOne.navigator.navigate(StubScene(), FakeRoute2)
        DiscoveryOne.navigator.navigate(StubScene(), FakeRoute3)

        assertEquals(
            listOf(FakeRoute1, FakeRoute2, FakeRoute3),
            FakeNavigator.routeHistorySpy
        )
    }

    @Test
    fun `WHEN navigating to a route for result given a token THEN navigator is called for result with same token`() {
        DiscoveryOne.navigator.navigateForResult(StubScene(), "key", FakeRoute1)

        assertEquals(
            listOf(Pair(FakeRoute1, "key")),
            FakeNavigator.ROUTE_FOR_RESULT_HISTORY_SPY
        )
    }

    @Test
    fun `WHEN registering 3 times for a result THEN navigator is called for registering a result 3 times`() {
        DiscoveryOne.navigator.onResult(StubScene(), "key1", Int::class) { }
        DiscoveryOne.navigator.onResult(StubScene(), "key2", Int::class) { }
        DiscoveryOne.navigator.onResult(StubScene(), "key3", Int::class) { }

        assertEquals(
            3,
            FakeNavigator.registerResultSpyCounter
        )
    }

    @Test
    fun `Given a previous route showed, WHEN navigating to a new one and closing last route THEN last visible route is the only in the stack`() {
        DiscoveryOne.navigator.navigate(StubScene(), FakeRoute1)

        DiscoveryOne.navigator.navigate(StubScene(), FakeRoute2)
        DiscoveryOne.navigator.close(StubScene())

        assertEquals(
            listOf(FakeRoute1),
            FakeNavigator.routeHistorySpy
        )
    }

    @Test
    fun `WHEN navigating to a new route and closing it with result THEN closeWithResultSpy list contains the value set as result when closing the route`() {
        DiscoveryOne.navigator.navigate(StubScene(), FakeRoute1)

        DiscoveryOne.navigator.closeWithResult(StubScene(), "fake-result")

        assertEquals(
            listOf("fake-result"),
            FakeNavigator.closeWithResultSpy
        )
    }

    object FakeNavigator : Navigator {

        var routeHistorySpy: MutableList<AbstractRoute> = mutableListOf()
        val ROUTE_FOR_RESULT_HISTORY_SPY: MutableList<Pair<AbstractRoute, String>> =
            mutableListOf()
        var registerResultSpyCounter = 0
        var closeWithResultSpy: MutableList<Any> = mutableListOf()

        override fun navigate(scene: Scene, route: AbstractRoute) {
            routeHistorySpy.add(route)
        }

        override fun navigateForResult(
            scene: Scene,
            key: String,
            route: AbstractRoute
        ) {
            ROUTE_FOR_RESULT_HISTORY_SPY.add(Pair(route, key))
        }

        override fun <T : Any> onResult(
            scene: Scene,
            key: String,
            resultClass: KClass<T>,
            action: (T) -> Unit
        ) {
            registerResultSpyCounter++
        }

        override fun close(scene: Scene) {
            routeHistorySpy.removeAt(routeHistorySpy.lastIndex)
        }

        override fun <T> closeWithResult(scene: Scene, result: T) {
            routeHistorySpy.removeAt(routeHistorySpy.lastIndex)
            closeWithResultSpy.add(result as Any)
        }

        fun clear() {
            routeHistorySpy.clear()
            ROUTE_FOR_RESULT_HISTORY_SPY.clear()
            closeWithResultSpy.clear()
            registerResultSpyCounter = 0
        }
    }

    object FakeRoute1 : AbstractRoute {
        override val clazz: KClass<*> = Any::class
    }

    object FakeRoute2 : AbstractRoute {
        override val clazz: KClass<*> = Any::class
    }

    object FakeRoute3 : AbstractRoute {
        override val clazz: KClass<*> = Any::class
    }
}