package com.discoveryone

import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.destination.AbstractDestination
import java.util.Stack

object Navigator {

    internal val stack = Stack<AppCompatActivity>()

    private val navigationHandler: NavigationHandler = AndroidNavigationHandler(stack)

    fun navigate(destination: AbstractDestination) {
        navigationHandler.navigate(destination)
    }
}