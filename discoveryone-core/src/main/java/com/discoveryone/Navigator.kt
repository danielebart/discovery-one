package com.discoveryone

import com.discoveryone.destinations.AbstractDestination
import com.discoveryone.stubs.StubNavigationHandler
import kotlin.reflect.KClass

object Navigator : NavigationHandler {

    private var navigationHandler: NavigationHandler =
        StubNavigationHandler()

    fun initialize(navigationHandler: NavigationHandler) {
        this.navigationHandler = navigationHandler
    }

    override fun navigate(destination: AbstractDestination) {
        navigationHandler.navigate(destination)
    }

    override fun <T> navigateForResult(
        destination: AbstractDestination,
        token: ResultToken
    ) {
        navigationHandler.navigateForResult<T>(destination, token)
    }

    override fun <T : Any> registerResult(
        resultClass: KClass<T>,
        action: (T) -> Unit
    ): ResultToken =
        navigationHandler.registerResult(resultClass, action)
}
