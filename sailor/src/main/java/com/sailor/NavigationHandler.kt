package com.sailor

import com.sailor.destination.AbstractDestination

interface NavigationHandler {

    fun navigate(destination: AbstractDestination)
}