package com.discoveryone.destinations

import kotlin.reflect.KClass

interface AbstractDestination {
    val clazz: KClass<*>
}

interface FragmentDestination :
    AbstractDestination {

    val containerId: Int
}

interface ActivityDestination :
    AbstractDestination