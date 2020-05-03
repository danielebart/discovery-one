package com.discoveryone.core

import com.discoveryone.Scene
import com.discoveryone.destinations.AbstractDestination
import kotlin.reflect.KClass

class StubScene : Scene {

    override fun navigate(destination: AbstractDestination) = Unit

    override fun navigateForResult(key: String, destination: AbstractDestination) = Unit

    override fun <T : Any> onResult(key: String, resultClass: KClass<T>, action: (T) -> Unit) = Unit

    override fun <T> closeWithResult(result: T?) = Unit
}