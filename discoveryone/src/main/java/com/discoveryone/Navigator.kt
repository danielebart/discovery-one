package com.discoveryone

import androidx.fragment.app.FragmentActivity
import com.discoveryone.destination.AbstractDestination
import java.util.Stack

object Navigator {

    internal val stack = Stack<FragmentActivity>()

    private val navigationHandler: NavigationHandler = AndroidNavigationHandler(stack)

    fun navigate(destination: AbstractDestination) {
        navigationHandler.navigate(destination)
    }

    fun navigateBack() {
        navigationHandler.navigateBack()
    }
}