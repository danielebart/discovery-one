package com.discoveryone

import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.KClass

interface Navigator {

    fun navigate(route: AbstractRoute)

    fun navigateForResult(route: AbstractRoute)

    fun <T : Any, R : AbstractRoute> onResult(
        routeClass: KClass<R>,
        resultClass: KClass<T>,
        action: (T) -> Unit
    )

    fun close()

    fun <T> closeWithResult(result: T)
}