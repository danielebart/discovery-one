package com.sailor

import androidx.appcompat.app.AppCompatActivity
import com.sailor.destination.AbstractDestination
import java.util.Stack

object Navigator {

    val stack = Stack<AppCompatActivity>()

    private val navigationHandler: NavigationHandler = AndroidNavigationHandler

    fun navigate(destination: AbstractDestination) {
        navigationHandler.navigate(destination)
    }
}