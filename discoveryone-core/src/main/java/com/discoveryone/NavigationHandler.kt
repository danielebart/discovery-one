package com.discoveryone

import com.discoveryone.destinations.AbstractDestination
import kotlin.reflect.KClass

interface NavigationHandler {

    fun navigate(destination: AbstractDestination)

    fun <T> navigateForResult(destination: AbstractDestination, token: ResultToken)

    fun <T : Any> registerResult(resultClass: KClass<T>, action: (T) -> Unit): ResultToken
}