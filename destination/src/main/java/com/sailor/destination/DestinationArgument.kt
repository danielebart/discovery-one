package com.sailor.destination

import kotlin.reflect.KClass

annotation class DestinationArgument(val name: String, val type: KClass<*>)