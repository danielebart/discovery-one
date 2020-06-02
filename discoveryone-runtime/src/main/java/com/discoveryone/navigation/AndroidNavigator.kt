package com.discoveryone.navigation

import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.navigation.result.ResultRegistry
import com.discoveryone.routes.AbstractRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import com.discoveryone.routes.GeneratedFragmentRoute
import kotlin.reflect.KClass

internal class AndroidNavigator constructor(
    private val activity: FragmentActivity,
    private val navigationContext: NavigationContext
) : Navigator {

    override fun navigate(route: AbstractRoute) {
        when (route) {
            is GeneratedFragmentRoute -> FragmentNavigation.navigate(
                currentActivity = activity,
                route = route
            )
            is GeneratedActivityRoute -> ActivityNavigation.navigate(
                activity,
                route = route
            )
            is GeneratedDialogFragmentRoute -> DialogFragmentNavigation.navigate(
                activity,
                route = route
            )
        }
    }

    override fun navigateForResult(route: AbstractRoute) {
        when (route) {
            is GeneratedFragmentRoute -> FragmentNavigation.navigateForResult(
                currentActivity = activity,
                route = route,
                key = ResultRegistry.buildSimpleResultKey(route::class)
            )
            is GeneratedActivityRoute -> ActivityNavigation.navigateForResult(
                currentActivity = activity,
                route = route,
                navigationContext = navigationContext
            )
            is GeneratedDialogFragmentRoute -> DialogFragmentNavigation.navigateForResult(
                currentActivity = activity,
                route = route,
                key = ResultRegistry.buildSimpleResultKey(route::class)
            )
        }
    }

    override fun <T : Any, R : AbstractRoute> onResult(
        routeClass: KClass<R>,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        ResultRegistry.registerResultAction(
            navigationContext = navigationContext,
            routeClass = routeClass,
            resultClass = resultClass,
            action = action,
            currentActivity = activity
        )
    }

    override fun close() {
        when (navigationContext.componentType) {
            NavigationContext.ComponentType.ACTIVITY -> ActivityNavigation.close(activity)
            NavigationContext.ComponentType.FRAGMENT -> FragmentNavigation.close(activity)
            NavigationContext.ComponentType.DIALOG_FRAGMENT ->
                DialogFragmentNavigation.close(navigationContext, activity)
        }
    }

    override fun <T> closeWithResult(result: T) {
        when (navigationContext.componentType) {
            NavigationContext.ComponentType.ACTIVITY -> ActivityNavigation.closeWithResult(
                currentActivity = activity,
                result = result
            )
            NavigationContext.ComponentType.FRAGMENT -> {
                FragmentNavigation.closeWithResult(
                    navigationContext = navigationContext,
                    currentActivity = activity,
                    result = result
                )
            }
            NavigationContext.ComponentType.DIALOG_FRAGMENT -> {
                DialogFragmentNavigation.closeWithResult(
                    currentActivity = activity,
                    result = result,
                    navigationContext = navigationContext
                )
            }
        }
    }
}
