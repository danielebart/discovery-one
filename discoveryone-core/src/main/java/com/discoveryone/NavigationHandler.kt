package com.discoveryone

import com.discoveryone.destinations.AbstractDestination

interface NavigationHandler {

    fun navigate(destination: AbstractDestination)
}