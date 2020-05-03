package com.discoveryone

import com.discoveryone.destinations.AbstractDestination
import com.discoveryone.stubs.StubNavigator
import kotlin.reflect.KClass

object DiscoveryOne : Navigator {

    private var navigator: Navigator =
        StubNavigator()

    fun install(navigator: Navigator) {
        this.navigator = navigator
    }

    override fun navigate(scene: Scene, destination: AbstractDestination) {
        navigator.navigate(scene, destination)
    }

    override fun navigateForResult(scene: Scene, key: String, destination: AbstractDestination) {
        navigator.navigateForResult(scene, key, destination)
    }

    override fun <T : Any> onResult(
        scene: Scene,
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        navigator.onResult(scene, key, resultClass, action)
    }

    override fun close(scene: Scene) {
        navigator.close(scene)
    }

    override fun <T> closeWithResult(scene: Scene, result: T) {
        navigator.closeWithResult(scene, result)
    }
}
