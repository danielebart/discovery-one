package com.discoveryone.stubs

import com.discoveryone.Navigator
import com.discoveryone.Scene
import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.KClass

class StubNavigator : Navigator {

    override fun navigate(scene: Scene, route: AbstractRoute) = Unit

    override fun navigateForResult(scene: Scene, key: String, route: AbstractRoute) =
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