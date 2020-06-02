package com.discoveryone.navigation

import android.os.Handler
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.extensions.extractPropertiesForBundle
import com.discoveryone.navigation.result.ResultRegistry.DEFAULT_INTENT_EXTRA_KEY
import com.discoveryone.routes.GeneratedFragmentRoute
import kotlin.reflect.KClass

internal object FragmentNavigation {

    fun navigate(currentActivity: FragmentActivity, route: GeneratedFragmentRoute) {
        val fragmentClass = route.clazz as KClass<Fragment>
        val userArgs = route.extractPropertiesForBundle().toTypedArray()

        currentActivity.supportFragmentManager.beginTransaction()
            .addToBackStack("")
            .replace(route.containerId, fragmentClass.java, bundleOf(*userArgs))
            .commit()
    }

    fun navigateForResult(
        currentActivity: FragmentActivity,
        route: GeneratedFragmentRoute,
        key: String
    ) {
        val fragmentClass = route.clazz as KClass<Fragment>
        val userArgs = route.extractPropertiesForBundle().toTypedArray()
        val fragmentArgs = bundleOf(
            *userArgs,
            FRAGMENT_NAVIGATION_FOR_RESULT_KEY to key
        )

        currentActivity.supportFragmentManager.beginTransaction()
            .addToBackStack("")
            .replace(route.containerId, fragmentClass.java, fragmentArgs)
            .commit()
    }

    fun close(currentActivity: FragmentActivity) {
        Handler(Looper.getMainLooper()).post {
            currentActivity.onBackPressed()
        }
    }

    fun <T> closeWithResult(
        navigationContext: NavigationContext,
        currentActivity: FragmentActivity,
        result: T
    ) {
        val key = navigationContext.extra ?: run {
            close(currentActivity)
            return
        }
        val bundleResult = bundleOf(DEFAULT_INTENT_EXTRA_KEY to result)
        currentActivity.supportFragmentManager.setFragmentResult(key, bundleResult)
        Handler(Looper.getMainLooper()).post {
            currentActivity.onBackPressed()
        }
    }

    internal const val FRAGMENT_NAVIGATION_FOR_RESULT_KEY = "FRAGMENT_NAVIGATION_FOR_RESULT_KEY"
}