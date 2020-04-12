package com.discoveryone

import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.discoveryone.destination.AbstractDestination
import com.discoveryone.destination.ActivityDestination
import com.discoveryone.destination.FragmentDestination
import kotlin.reflect.KClass

object AndroidNavigationHandler : NavigationHandler {

    override fun navigate(destination: AbstractDestination) {
        when (destination) {
            is FragmentDestination -> navigateToFragment(destination.clazz, destination.containerId)
            is ActivityDestination -> navigateToActivity(destination.clazz)
        }
    }

    private fun navigateToActivity(clazz: KClass<*>) {
        val currentActivity = Navigator.stack.peek()
        currentActivity.startActivity(Intent(currentActivity, clazz.java))
    }

    private fun navigateToFragment(clazz: KClass<*>, @IdRes containerId: Int) {
        val clazzFragment = clazz as KClass<Fragment>
        val currentActivity = Navigator.stack.peek()
        currentActivity.supportFragmentManager.beginTransaction()
            .replace(containerId, clazzFragment.java.newInstance())
            .commit()
    }
}