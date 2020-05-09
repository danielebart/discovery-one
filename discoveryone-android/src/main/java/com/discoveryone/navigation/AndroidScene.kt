package com.discoveryone.navigation

import com.discoveryone.DiscoveryOne
import com.discoveryone.Scene
import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.KClass

class AndroidScene internal constructor(
    internal val componentType: ComponentType,
    internal val instanceHashCode: Int
) : Scene {

    override fun navigate(route: AbstractRoute) {
        DiscoveryOne.navigator.navigate(this, route)
    }

    override fun navigateForResult(key: String, route: AbstractRoute) {
        DiscoveryOne.navigator.navigateForResult(this, key, route)
    }

    override fun <T : Any> onResult(
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        DiscoveryOne.navigator.onResult(this, key, resultClass, action)
    }

    override fun <T> closeWithResult(result: T?) {
        if (result != null) {
            DiscoveryOne.navigator.closeWithResult(this, result)
        } else {
            DiscoveryOne.navigator.close(this)
        }
    }

    enum class ComponentType {
        ACTIVITY, FRAGMENT
    }
}