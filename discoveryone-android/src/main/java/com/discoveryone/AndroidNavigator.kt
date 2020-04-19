package com.discoveryone

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.discoveryone.destinations.AbstractDestination
import com.discoveryone.destinations.ActivityDestination
import com.discoveryone.destinations.FragmentDestination
import com.discoveryone.result.AndroidResultRegistry
import com.discoveryone.result.RegistryKey
import kotlin.reflect.KClass

class AndroidNavigator(application: Application) : NavigationHandler {

    init {
        application.registerActivityLifecycleCallbacks(NavigatorActivityLifecycleCallback)
    }

    private val currentActivity: FragmentActivity
        get() = ActivityStackContainer.peek()

    override fun navigate(destination: AbstractDestination) {
        when (destination) {
            is FragmentDestination -> FragmentNavigation.navigate(currentActivity, destination)
            is ActivityDestination -> ActivityNavigation.navigate(currentActivity, destination)
        }
    }

    override fun <T> navigateForResult(
        destination: AbstractDestination,
        token: ResultToken
    ) {
        when (destination) {
            is FragmentDestination -> TODO()
            is ActivityDestination -> ActivityNavigation.navigateForResult<T>(
                currentActivity,
                destination,
                AndroidResultRegistry.get(RegistryKey(currentActivity.hashCode(), token))
            )
        }
    }

    override fun <T : Any> registerResult(
        resultClass: KClass<T>,
        action: (T) -> Unit
    ): ResultToken =
        AndroidResultRegistry.registerResultAction(resultClass, action)
}
