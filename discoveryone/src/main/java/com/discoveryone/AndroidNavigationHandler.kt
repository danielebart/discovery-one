package com.discoveryone

import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.destination.AbstractDestination
import com.discoveryone.destination.ActivityDestination
import com.discoveryone.destination.FragmentDestination
import com.discoveryone.destination.InternalDestinationArgumentMarker
import java.util.Stack
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

internal class AndroidNavigationHandler(
    private val activityStack: Stack<FragmentActivity>
) : NavigationHandler {

    override fun navigate(destination: AbstractDestination) {
        val currentActivity = activityStack.peek()
        when (destination) {
            is FragmentDestination -> destination.navigateToFragment(currentActivity)
            is ActivityDestination -> destination.navigateToActivity(currentActivity)
        }
    }

    override fun navigateBack() {
        activityStack.peek().onBackPressed()
    }

    private fun ActivityDestination.navigateToActivity(currentActivity: Activity) {
        val arguments = extractArgumentsFromDestination().toTypedArray()
        val intent = Intent(currentActivity, clazz.java).putExtras(bundleOf(*arguments))

        currentActivity.startActivity(intent)
    }

    private fun FragmentDestination.navigateToFragment(currentActivity: FragmentActivity) {
        val fragmentClass = clazz as KClass<Fragment>
        val arguments = extractArgumentsFromDestination().toTypedArray()

        currentActivity.supportFragmentManager.beginTransaction()
            .replace(containerId, fragmentClass.java, bundleOf(*arguments), null)
            .commit()
    }

    private fun AbstractDestination.extractArgumentsFromDestination(): List<Pair<String, Any?>> =
        this::class.memberProperties.filter { property ->
            property.annotations.map { annotation -> annotation.annotationClass }
                .contains(InternalDestinationArgumentMarker::class)
        }
            .map { property ->
                Pair(property.name, property.getter.call(this))
            }
}
