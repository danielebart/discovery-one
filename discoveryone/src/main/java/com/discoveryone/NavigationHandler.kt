package com.discoveryone

import com.discoveryone.destination.AbstractDestination

interface NavigationHandler {

    fun navigate(destination: AbstractDestination)
}