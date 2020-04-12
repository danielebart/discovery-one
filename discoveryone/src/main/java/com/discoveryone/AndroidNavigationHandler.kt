package com.discoveryone

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.discoveryone.destination.AbstractDestination
import com.discoveryone.destination.ActivityDestination
import com.discoveryone.destination.FragmentDestination
import com.discoveryone.destination.InternalDestinationArgumentMarker
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

internal object AndroidNavigationHandler : NavigationHandler {

    override fun navigate(destination: AbstractDestination) {
        when (destination) {
            is FragmentDestination -> destination.navigateToFragment()
            is ActivityDestination -> destination.navigateToActivity()
        }
    }

    private fun ActivityDestination.navigateToActivity() {
        val currentActivity = Navigator.stack.peek()
        val arguments = this::class.memberProperties.filter { property ->
            property.annotations.map { annotation -> annotation.annotationClass }
                .contains(InternalDestinationArgumentMarker::class)
        }
            .map { property ->
                Pair(property.name, property.getter.call(this))
            }.toTypedArray()

        val intent = Intent(currentActivity, clazz.java).putExtras(bundleOf(*arguments))
        currentActivity.startActivity(intent)
    }

    private fun FragmentDestination.navigateToFragment() {
        val clazzFragment = clazz as KClass<Fragment>
        val currentActivity = Navigator.stack.peek()
        currentActivity.supportFragmentManager.beginTransaction()
            .replace(containerId, clazzFragment.java.newInstance())
            .commit()
    }
}