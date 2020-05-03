package com.discoveryone.stubs

import com.discoveryone.Navigator
import com.discoveryone.Scene
import com.discoveryone.destinations.AbstractDestination
import kotlin.reflect.KClass

class StubNavigator : Navigator {

    override fun navigate(scene: Scene, destination: AbstractDestination) = Unit

    override fun navigateForResult(scene: Scene, key: String, destination: AbstractDestination) =
        Unit

    override fun <T : Any> onResult(
        scene: Scene,
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) = Unit

    override fun close(scene: Scene) = Unit

    override fun <T> closeWithResult(scene: Scene, result: T) = Unit
}