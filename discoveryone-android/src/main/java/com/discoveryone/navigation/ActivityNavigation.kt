package com.discoveryone.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.discoveryone.exceptions.ActivityNotFoundOnResultRegistration
import com.discoveryone.exceptions.NoActionRegisteredForGivenKeyException
import com.discoveryone.extensions.extractPropertiesForBundle
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.navigation.result.ActionLauncher.DEFAULT_INTENT_EXTRA_KEY
import com.discoveryone.navigation.result.ActivityResultLauncherFactory
import com.discoveryone.routes.GeneratedActivityRoute
import kotlin.reflect.KClass

internal object ActivityNavigation {

    private val activityResultLauncherMap: MutableMap<ActivityResultLauncherMapKey, ActivityResultLauncher<Intent>> =
        mutableMapOf()

    fun navigate(currentActivity: FragmentActivity, route: GeneratedActivityRoute) {
        val properties = route.extractPropertiesForBundle().toTypedArray()
        val intent = Intent(currentActivity, route.clazz.java).putExtras(bundleOf(*properties))

        currentActivity.startActivity(intent)
    }

    fun navigateForResult(
        scene: AndroidScene,
        currentActivity: FragmentActivity,
        route: GeneratedActivityRoute,
        userKey: String
    ) {
        val key = ActivityResultLauncherMapKey(scene.instanceHashCode, userKey)
        val activityResultLauncher =
            activityResultLauncherMap[key] ?: throw NoActionRegisteredForGivenKeyException()
        val properties = route.extractPropertiesForBundle().toTypedArray()
        val intent = Intent(currentActivity, route.clazz.java).putExtras(bundleOf(*properties))
        activityResultLauncherMap.remove(key)
        activityResultLauncher.launch(intent)
    }

    fun <T : Any> registerResultAction(
        scene: AndroidScene,
        userKey: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        val key = ActivityResultLauncherMapKey(scene.instanceHashCode, userKey)
        if (ActivityStackContainer.isEmpty().not()) {
            val currentActivity = ActivityStackContainer.peek()
            activityResultLauncherMap[key] =
                ActivityResultLauncherFactory.create(resultClass, currentActivity, action)
        } else {
            throw ActivityNotFoundOnResultRegistration()
        }
    }

    internal fun close(currentActivity: FragmentActivity) {
        currentActivity.finish()
    }

    internal fun <T> closeWithResult(currentActivity: FragmentActivity, result: T) {
        currentActivity.setResult(
            Activity.RESULT_OK,
            Intent().putExtras(bundleOf(DEFAULT_INTENT_EXTRA_KEY to result))
        )
        currentActivity.finish()
    }

    internal fun unregisterActivityResultLauncher(activity: Activity) {
        activityResultLauncherMap
            .filter { (key, _) ->
                key.activityInstanceHashCode == activity.hashCode()
            }.forEach { (key, _) ->
                activityResultLauncherMap.remove(key)
            }
    }

    private data class ActivityResultLauncherMapKey(
        val activityInstanceHashCode: Int,
        val userKey: String
    )
}