package com.discoveryone.core

import com.discoveryone.Scene
import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.KClass

class StubScene : Scene {

    override fun navigate(route: AbstractRoute) = Unit

    override fun navigateForResult(key: String, route: AbstractRoute) = Unit

    override fun <T : Any> onResult(key: String, resultClass: KClass<T>, action: (T) -> Unit) = Unit

    override fun <T> closeWithResult(result: T?) = Unit
}