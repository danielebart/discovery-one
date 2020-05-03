package com.discoveryone.core

class DiscoveryOneTest {

//    @Before
//    fun setup() {
//        DiscoveryOne.install(FakeNavigator)
//    }

//    @Test
//    fun `WHEN navigating to 3 different destinations THEN handler is called 3 times`() {
//        DiscoveryOne.navigate(FakeDestination1)
//        DiscoveryOne.navigate(FakeDestination2)
//        DiscoveryOne.navigate(FakeDestination3)
//
//        assertEquals(
//            listOf(FakeDestination1, FakeDestination2, FakeDestination3),
//            FakeNavigator.destinationHistorySpy
//        )
//    }
//
//    @Test
//    fun `WHEN navigating to a destination for result given a token THEN handler is called for result with same token`() {
//        DiscoveryOne.navigateForResult(FakeDestination1, ResultToken(345))
//
//        assertEquals(
//            listOf(Pair(FakeDestination1, ResultToken(345))),
//            FakeNavigator.destinationForResultHistorySpy
//        )
//    }
//
//    @Test
//    fun `WHEN registering 3 times for a result THEN handler is called for registering a result 3 times`() {
//        DiscoveryOne.onResult<Int> {  }
//        DiscoveryOne.onResult<Int> {  }
//        DiscoveryOne.onResult<Int> {  }
//
//        assertEquals(
//            3,
//            FakeNavigator.registerResultSpyCounter
//        )
//    }
//
//    object FakeNavigator : Navigator {
//
//        var destinationHistorySpy: MutableList<AbstractDestination> = mutableListOf()
//        val destinationForResultHistorySpy: MutableList<Pair<AbstractDestination, String>> = mutableListOf()
//        var registerResultSpyCounter = 0
//
//        override fun navigate(scene: Scene, destination: AbstractDestination) {
//            destinationHistorySpy.add(destination)
//        }
//
//        override fun navigateForResult(
//            scene: Scene,
//            key: String,
//            destination: AbstractDestination
//        ) {
//            destinationForResultHistorySpy.add(Pair(destination, key))
//        }
//
//        override fun <T : Any> onResult(
//            scene: Scene,
//            key: String,
//            resultClass: KClass<T>,
//            action: (T) -> Unit
//        ) {
//            registerResultSpyCounter++
//        }
//
//        override fun close(scene: Scene) {
//
//        }
//
//        override fun <T> closeWithResult(scene: Scene, result: T) {
//
//        }
//    }
//
//    object FakeDestination1 : AbstractDestination {
//        override val clazz: KClass<*> = Any::class
//    }
//
//    object FakeDestination2 : AbstractDestination {
//        override val clazz: KClass<*> = Any::class
//    }
//
//    object FakeDestination3 : AbstractDestination {
//        override val clazz: KClass<*> = Any::class
//    }
}