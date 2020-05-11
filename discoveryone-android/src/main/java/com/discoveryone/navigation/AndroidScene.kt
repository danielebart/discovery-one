package com.discoveryone.navigation

import kotlin.reflect.KClass

data class AndroidScene internal constructor(
    internal val componentType: ComponentType,
    internal val instanceHashCode: Int,
    internal val componentClass: KClass<*>
) {

    enum class ComponentType {
        ACTIVITY, FRAGMENT, DIALOG_FRAGMENT
    }
}