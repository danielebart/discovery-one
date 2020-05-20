package com.discoveryone.navigation

import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.initialization.ActivityInterceptor
import com.discoveryone.navigation.result.ResultRegistry
import com.discoveryone.routes.AbstractRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import com.discoveryone.routes.GeneratedFragmentRoute
import kotlin.reflect.KClass

internal class AndroidNavigator constructor(
    private val navigationContext: NavigationContext
) : Navigator {

    private val currentActivity: FragmentActivity
        get() = if (navigationContext.componentType == NavigationContext.ComponentType.ACTIVITY) {
            ActivityInterceptor.getActivityFromNavigationContext(navigationContext)
        } else {
            ActivityInterceptor.getLast()
        }

    override fun navigate(route: AbstractRoute) {
        when (route) {
            is GeneratedFragmentRoute -> FragmentNavigation.navigate(
                currentActivity = currentActivity,
                route = route
            )
            is GeneratedActivityRoute -> ActivityNavigation.navigate(
                currentActivity,
                route = route
            )
            is GeneratedDialogFragmentRoute -> DialogFragmentNavigation.navigate(
                currentActivity,
                route = route
            )
        }
    }

    override fun navigateForResult(route: AbstractRoute) {
        when (route) {
            is GeneratedFragmentRoute -> FragmentNavigation.navigateForResult(
                currentActivity = currentActivity,
                route = route,
                key = ResultRegistry.buildSimpleResultKey(route::class)
            )
            is GeneratedActivityRoute -> ActivityNavigation.navigateForResult(
                currentActivity = currentActivity,
                route = route,
                navigationContext = navigationContext
            )
            is GeneratedDialogFragmentRoute -> DialogFragmentNavigation.navigateForResult(
                currentActivity = currentActivity,
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
            action = action
        )
    }

    override fun close() {
        when (navigationContext.componentType) {
            NavigationContext.ComponentType.ACTIVITY -> ActivityNavigation.close(currentActivity)
            NavigationContext.ComponentType.FRAGMENT -> FragmentNavigation.close(currentActivity)
            NavigationContext.ComponentType.DIALOG_FRAGMENT -> DialogFragmentNavigation.close(
                navigationContext = navigationContext,
                currentActivity = currentActivity
            )
        }
    }

    override fun <T> closeWithResult(result: T) {
        when (navigationContext.componentType) {
            NavigationContext.ComponentType.ACTIVITY -> ActivityNavigation.closeWithResult(
                currentActivity = currentActivity,
                result = result
            )
            NavigationContext.ComponentType.FRAGMENT -> {
                FragmentNavigation.closeWithResult(
                    navigationContext = navigationContext,
                    currentActivity = currentActivity,
                    result = result
                )
            }
            NavigationContext.ComponentType.DIALOG_FRAGMENT -> {
                DialogFragmentNavigation.closeWithResult(
                    currentActivity = currentActivity,
                    result = result,
                    navigationContext = navigationContext
                )
            }
        }
    }
}
