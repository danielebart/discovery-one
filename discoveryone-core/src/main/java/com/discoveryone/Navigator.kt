package com.discoveryone

import com.discoveryone.destinations.AbstractDestination
import kotlin.reflect.KClass

interface Navigator {

    fun navigate(scene: Scene, destination: AbstractDestination)

    fun navigateForResult(scene: Scene, key: String, destination: AbstractDestination)

    fun <T : Any> onResult(
        scene: Scene,
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    )

    fun close(scene: Scene)

    fun <T> closeWithResult(scene: Scene, result: T)
}