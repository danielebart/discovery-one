package com.discoveryone.navigation

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.Scene
import com.discoveryone.destinations.AbstractDestination
import com.discoveryone.destinations.ActivityDestination
import com.discoveryone.destinations.FragmentDestination
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.initialization.NavigatorActivityLifecycleCallback
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

    override fun navigate(scene: Scene, destination: AbstractDestination) {
        scene as AndroidScene
        when (destination) {
            is FragmentDestination -> FragmentNavigation.navigate(
                scene.currentActivity,
                destination
            )
            is ActivityDestination -> ActivityNavigation.navigate(
                scene.currentActivity,
                destination
            )
        }
    }

    override fun navigateForResult(scene: Scene, key: String, destination: AbstractDestination) {
        scene as AndroidScene
        when (destination) {
            is FragmentDestination -> FragmentNavigation.navigateForResult(
                currentActivity = scene.currentActivity,
                destination = destination,
                key = key
            )
            is ActivityDestination -> ActivityNavigation.navigateForResult(
                currentActivity = scene.currentActivity,
                destination = destination,
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
