package com.discoveryone

import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.KClass

interface Navigator {

    fun navigate(scene: Scene, route: AbstractRoute)

    fun navigateForResult(scene: Scene, key: String, route: AbstractRoute)

    fun <T : Any> onResult(
        scene: Scene,
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    )

    fun close(scene: Scene)

    fun <T> closeWithResult(scene: Scene, result: T)
}