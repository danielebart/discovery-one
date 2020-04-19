package com.discoveryone.annotations

import kotlin.reflect.KClass

annotation class DestinationArgument(val name: String, val type: KClass<*>)