package com.discoveryone

import com.discoveryone.destinations.AbstractDestination
import java.util.Stack

class SimpleStackNavigationHandler : NavigationHandler {

    private val stack: Stack<AbstractDestination> = Stack()

    override fun navigate(destination: AbstractDestination) {
        stack.push(destination)
    }
}