package com.discoveryone.destination

import kotlin.reflect.KClass

interface AbstractDestination {
    val name: String
    val clazz: KClass<*>
}

interface FragmentDestination : AbstractDestination {

    val containerId: Int
}

interface ActivityDestination : AbstractDestination