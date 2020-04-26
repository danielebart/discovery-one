package com.discoveryone.stubs

import com.discoveryone.NavigationHandler
import com.discoveryone.ResultToken
import com.discoveryone.destinations.AbstractDestination
import java.util.Stack
import kotlin.reflect.KClass

class StubNavigationHandler : NavigationHandler {

    private val stack: Stack<AbstractDestination> = Stack()

    override fun navigate(destination: AbstractDestination) {
        stack.push(destination)
    }

    override fun navigateForResult(
        destination: AbstractDestination,
        token: ResultToken
    ) {
        stack.push(destination)
    }

    override fun <T : Any> registerResult(
        resultClass: KClass<T>,
        action: (T) -> Unit
    ): ResultToken = ResultToken(0)
}