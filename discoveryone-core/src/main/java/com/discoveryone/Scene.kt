package com.discoveryone

import com.discoveryone.destinations.AbstractDestination
import kotlin.reflect.KClass

interface Scene {

    fun navigate(destination: AbstractDestination)

    fun navigateForResult(key: String, destination: AbstractDestination)

    fun <T : Any> onResult(key: String, resultClass: KClass<T>, action: (T) -> Unit)

    fun <T> close(result: T? = null)
}