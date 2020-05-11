package com.discoveryone.navigation

import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.routes.AbstractRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import com.discoveryone.routes.GeneratedFragmentRoute
import kotlin.reflect.KClass

class AndroidNavigator internal constructor(private val scene: AndroidScene) : Navigator {

    private val currentActivity: FragmentActivity
        get() = if (scene.componentType == AndroidScene.ComponentType.ACTIVITY) {
            ActivityStackContainer.getByHashCode(scene.instanceHashCode)
        } else {
            ActivityStackContainer.peek()
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

    override fun navigateForResult(key: String, route: AbstractRoute) {
        when (route) {
            is GeneratedFragmentRoute -> FragmentNavigation.navigateForResult(
                currentActivity = currentActivity,
                route = route,
                key = key
            )
            is GeneratedActivityRoute -> ActivityNavigation.navigateForResult(
                currentActivity = currentActivity,
                route = route,
                userKey = key,
                scene = scene
            )
            is GeneratedDialogFragmentRoute -> DialogFragmentNavigation.navigateForResult(
                currentActivity = currentActivity,
                route = route,
                key = key
            )
        }
    }

    override fun <T : Any> onResult(
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        when (scene.componentType) {
            AndroidScene.ComponentType.ACTIVITY -> ActivityNavigation.registerResultAction(
                userKey = key,
                resultClass = resultClass,
                action = action,
                scene = scene
            )
            AndroidScene.ComponentType.FRAGMENT -> FragmentNavigation.registerResultAction(
                scene = scene,
                key = key,
                resultClass = resultClass,
                action = action
            )
            AndroidScene.ComponentType.DIALOG_FRAGMENT -> DialogFragmentNavigation.registerResultAction(
                scene = scene,
                key = key,
                resultClass = resultClass,
                action = action
            )
        }
    }

    override fun close() {
        when (scene.componentType) {
            AndroidScene.ComponentType.ACTIVITY -> ActivityNavigation.close(currentActivity)
            AndroidScene.ComponentType.FRAGMENT -> FragmentNavigation.close(currentActivity)
            AndroidScene.ComponentType.DIALOG_FRAGMENT -> DialogFragmentNavigation.close(
                scene = scene,
                currentActivity = currentActivity
            )
        }
    }

    override fun <T> closeWithResult(result: T) {
        when (scene.componentType) {
            AndroidScene.ComponentType.ACTIVITY -> ActivityNavigation.closeWithResult(
                currentActivity = currentActivity,
                result = result
            )
            AndroidScene.ComponentType.FRAGMENT -> {
                FragmentNavigation.closeWithResult(
                    scene = scene,
                    currentActivity = currentActivity,
                    result = result
                )
            }
            AndroidScene.ComponentType.DIALOG_FRAGMENT -> {
                DialogFragmentNavigation.closeWithResult(
                    currentActivity = currentActivity,
                    result = result,
                    scene = scene
                )
            }
        }
    }
}
