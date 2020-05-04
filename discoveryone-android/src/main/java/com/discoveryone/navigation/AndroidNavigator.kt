package com.discoveryone.navigation

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.Scene
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.initialization.NavigatorActivityLifecycleCallback
import com.discoveryone.routes.AbstractRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.routes.GeneratedFragmentRoute
import kotlin.reflect.KClass

class AndroidNavigator(application: Application) : Navigator {

    init {
        application.registerActivityLifecycleCallbacks(NavigatorActivityLifecycleCallback)
    }

    private val AndroidScene.currentActivity: FragmentActivity
        get() = if (componentType == AndroidScene.ComponentType.ACTIVITY) {
            ActivityStackContainer.getByHashCode(instanceHashCode)
        } else {
            ActivityStackContainer.peek()
        }

    override fun navigate(scene: Scene, route: AbstractRoute) {
        scene as AndroidScene
        when (route) {
            is GeneratedFragmentRoute -> FragmentNavigation.navigate(
                scene.currentActivity,
                route
            )
            is GeneratedActivityRoute -> ActivityNavigation.navigate(
                scene.currentActivity,
                route
            )
        }
    }

    override fun navigateForResult(scene: Scene, key: String, route: AbstractRoute) {
        scene as AndroidScene
        when (route) {
            is GeneratedFragmentRoute -> FragmentNavigation.navigateForResult(
                currentActivity = scene.currentActivity,
                route = route,
                key = key
            )
            is GeneratedActivityRoute -> ActivityNavigation.navigateForResult(
                currentActivity = scene.currentActivity,
                route = route,
                userKey = key,
                scene = scene
            )
        }
    }

    override fun <T : Any> onResult(
        scene: Scene,
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        scene as AndroidScene
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
        }
    }

    override fun close(scene: Scene) {
        scene as AndroidScene
        when (scene.componentType) {
            AndroidScene.ComponentType.ACTIVITY -> ActivityNavigation.close(scene.currentActivity)
            AndroidScene.ComponentType.FRAGMENT -> FragmentNavigation.close(scene.currentActivity)
        }
    }

    override fun <T> closeWithResult(scene: Scene, result: T) {
        scene as AndroidScene

        when (scene.componentType) {
            AndroidScene.ComponentType.ACTIVITY -> ActivityNavigation.closeWithResult(
                currentActivity = scene.currentActivity,
                result = result
            )
            AndroidScene.ComponentType.FRAGMENT -> {
                FragmentNavigation.closeWithResult(
                    scene = scene,
                    currentActivity = scene.currentActivity,
                    result = result
                )
            }
        }
    }
}
