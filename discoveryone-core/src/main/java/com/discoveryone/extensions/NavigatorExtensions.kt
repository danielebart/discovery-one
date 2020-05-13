@file:JvmName("NavigatorExtensions")

package com.discoveryone.extensions

import com.discoveryone.Navigator
import com.discoveryone.routes.AbstractRoute

inline fun <reified T : Any, reified R : AbstractRoute> Navigator.onResult(noinline action: (T) -> Unit) {
    onResult(routeClass = R::class, resultClass = T::class, action = action)
}
