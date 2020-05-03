package com.discoveryone.core

import com.discoveryone.DiscoveryOne
import com.discoveryone.Navigator
import com.discoveryone.Scene
import com.discoveryone.destinations.AbstractDestination
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
    fun `WHEN navigating to 3 different destinations THEN navigator is called 3 times`() {
        DiscoveryOne.navigate(StubScene(), FakeDestination1)
        DiscoveryOne.navigate(StubScene(), FakeDestination2)
        DiscoveryOne.navigate(StubScene(), FakeDestination3)

        assertEquals(
            listOf(FakeDestination1, FakeDestination2, FakeDestination3),
            FakeNavigator.destinationHistorySpy
        )
    }

    @Test
    fun `WHEN navigating to a destination for result given a token THEN navigator is called for result with same token`() {
        DiscoveryOne.navigateForResult(StubScene(), "key", FakeDestination1)

        assertEquals(
            listOf(Pair(FakeDestination1, "key")),
            FakeNavigator.destinationForResultHistorySpy
        )
    }

    @Test
    fun `WHEN registering 3 times for a result THEN navigator is called for registering a result 3 times`() {
        DiscoveryOne.onResult(StubScene(), "key1", Int::class) { }
        DiscoveryOne.onResult(StubScene(), "key2", Int::class) { }
        DiscoveryOne.onResult(StubScene(), "key3", Int::class) { }

        assertEquals(
            3,
            FakeNavigator.registerResultSpyCounter
        )
    }

    @Test
    fun `Given a previous destination showed, WHEN navigating to a new one and closing last destination THEN last visible destination is the only in the stack`() {
        DiscoveryOne.navigate(StubScene(), FakeDestination1)

        DiscoveryOne.navigate(StubScene(), FakeDestination2)
        DiscoveryOne.close(StubScene())

        assertEquals(
            listOf(FakeDestination1),
            FakeNavigator.destinationHistorySpy
        )
    }

    @Test
    fun `WHEN navigating to a new Destination and closing it with result THEN closeWithResultSpy list contains the value set as result when closing the destination`() {
        DiscoveryOne.navigate(StubScene(), FakeDestination1)

        DiscoveryOne.closeWithResult(StubScene(), "fake-result")

        assertEquals(
            listOf("fake-result"),
            FakeNavigator.closeWithResultSpy
        )
    }

    object FakeNavigator : Navigator {

        var destinationHistorySpy: MutableList<AbstractDestination> = mutableListOf()
        val destinationForResultHistorySpy: MutableList<Pair<AbstractDestination, String>> =
            mutableListOf()
        var registerResultSpyCounter = 0
        var closeWithResultSpy: MutableList<Any> = mutableListOf()

        override fun navigate(scene: Scene, destination: AbstractDestination) {
            destinationHistorySpy.add(destination)
        }

        override fun navigateForResult(
            scene: Scene,
            key: String,
            destination: AbstractDestination
        ) {
            destinationForResultHistorySpy.add(Pair(destination, key))
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
            destinationHistorySpy.removeAt(destinationHistorySpy.lastIndex)
        }

        override fun <T> closeWithResult(scene: Scene, result: T) {
            destinationHistorySpy.removeAt(destinationHistorySpy.lastIndex)
            closeWithResultSpy.add(result as Any)
        }

        fun clear() {
            destinationHistorySpy.clear()
            destinationForResultHistorySpy.clear()
            closeWithResultSpy.clear()
            registerResultSpyCounter = 0
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