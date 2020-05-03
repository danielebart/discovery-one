package com.discoveryone.navigation

import com.discoveryone.DiscoveryOne
import com.discoveryone.Scene
import com.discoveryone.destinations.AbstractDestination
import kotlin.reflect.KClass

class AndroidScene internal constructor(
    internal val componentType: ComponentType,
    internal val instanceHashCode: Int
) : Scene {

    override fun navigate(destination: AbstractDestination) {
        DiscoveryOne.navigate(this, destination)
    }

    override fun navigateForResult(key: String, destination: AbstractDestination) {
        DiscoveryOne.navigateForResult(this, key, destination)
    }

    override fun <T : Any> onResult(
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        DiscoveryOne.onResult(this, key, resultClass, action)
    }

    override fun <T> close(result: T?) {
        if (result != null) {
            DiscoveryOne.closeWithResult(this, result)
        } else {
            DiscoveryOne.close(this)
        }
    }

    enum class ComponentType {
        ACTIVITY, FRAGMENT
    }
}