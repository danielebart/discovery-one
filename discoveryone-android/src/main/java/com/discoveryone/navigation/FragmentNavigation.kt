package com.discoveryone.navigation

import android.os.Handler
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.discoveryone.exceptions.FragmentNotFoundOnResultRegistration
import com.discoveryone.extensions.extractPropertiesForBundle
import com.discoveryone.extensions.firstFragmentOrNull
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.navigation.result.ActionLauncher
import com.discoveryone.navigation.result.ActionLauncher.launchActionOnResult
import com.discoveryone.routes.GeneratedFragmentRoute
import kotlin.reflect.KClass

internal object FragmentNavigation {

    fun navigate(currentActivity: FragmentActivity, route: GeneratedFragmentRoute) {
        val fragmentClass = route.clazz as KClass<Fragment>
        val arguments = route.extractPropertiesForBundle().toTypedArray()

        currentActivity.supportFragmentManager.beginTransaction()
            .addToBackStack("")
            .replace(route.containerId, fragmentClass.java, bundleOf(*arguments), null)
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
            .replace(route.containerId, fragmentClass.java, fragmentArgs, null)
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
        val fragment =
            currentActivity.firstFragmentOrNull { it.hashCode() == navigationContext.instanceHashCode }
        val key = fragment?.resultKey ?: run {
            close(currentActivity)
            return
        }
        val bundleResult = bundleOf(ActionLauncher.DEFAULT_INTENT_EXTRA_KEY to result)
        fragment.setFragmentResult(key, bundleResult)
        Handler(Looper.getMainLooper()).post {
            currentActivity.onBackPressed()
        }
    }

    fun <T : Any> registerResultAction(
        navigationContext: NavigationContext,
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        if (ActivityStackContainer.isEmpty().not()) {
            val currentActivity = ActivityStackContainer.peek()
            val fragment =
                currentActivity.firstFragmentOrNull { it.hashCode() == navigationContext.instanceHashCode }
                    ?: throw FragmentNotFoundOnResultRegistration()

            fragment.setFragmentResultListener(key) { _, bundle ->
                launchActionOnResult(bundle, resultClass, action)
            }
        } else {
            throw FragmentNotFoundOnResultRegistration()
        }
    }

    private val Fragment.resultKey: String?
        get() = arguments?.getString(FRAGMENT_NAVIGATION_FOR_RESULT_KEY)

    private const val FRAGMENT_NAVIGATION_FOR_RESULT_KEY = "FRAGMENT_NAVIGATION_FOR_RESULT_KEY"
}