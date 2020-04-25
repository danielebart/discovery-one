package com.discoveryone

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.destinations.FragmentDestination
import kotlin.reflect.KClass

object FragmentNavigation {

    internal fun navigate(currentActivity: FragmentActivity, destination: FragmentDestination) {
        val fragmentClass = destination.clazz as KClass<Fragment>
        val arguments = destination.extractArgumentsFromDestination().toTypedArray()

        currentActivity.supportFragmentManager.beginTransaction()
            .replace(destination.containerId, fragmentClass.java, bundleOf(*arguments), null)
            .commit()
    }
}