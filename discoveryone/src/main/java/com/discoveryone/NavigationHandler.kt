package com.discoveryone

import com.discoveryone.destination.AbstractDestination

internal interface NavigationHandler {

    fun navigate(destination: AbstractDestination)
}