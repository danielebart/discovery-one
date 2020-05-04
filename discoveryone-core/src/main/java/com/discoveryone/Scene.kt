package com.discoveryone

import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.KClass

interface Scene {

    fun navigate(route: AbstractRoute)

    fun navigateForResult(key: String, route: AbstractRoute)

    fun <T : Any> onResult(key: String, resultClass: KClass<T>, action: (T) -> Unit)

    fun <T> closeWithResult(result: T? = null)
}