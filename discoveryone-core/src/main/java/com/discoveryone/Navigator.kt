package com.discoveryone

import com.discoveryone.destinations.AbstractDestination

object Navigator {

    private var navigationHandler: NavigationHandler = SimpleStackNavigationHandler()

    fun setNavigationHandler(navigationHandler: NavigationHandler) {
        Navigator.navigationHandler = navigationHandler
    }

    fun navigate(destination: AbstractDestination) {
        navigationHandler.navigate(destination)
    }
}