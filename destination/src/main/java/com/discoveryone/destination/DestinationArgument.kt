package com.discoveryone.destination

import kotlin.reflect.KClass

annotation class DestinationArgument(val name: String, val type: KClass<*>)