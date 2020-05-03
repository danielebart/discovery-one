package com.discoveryone.annotations

import kotlin.reflect.KClass

annotation class RouteArgument(val name: String, val type: KClass<*>)