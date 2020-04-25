package com.discoveryone.result

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.discoveryone.ActivityStackContainer
import com.discoveryone.ResultToken
import java.lang.RuntimeException
import kotlin.reflect.KClass

internal object AndroidResultRegistry {

    private val actionResultActionMap: MutableMap<RegistryKey, ActivityResultLauncher<Intent>> =
        mutableMapOf()

    fun unregisterAllActionsForActivity(activity: Activity) {
        actionResultActionMap
            .filter { (key, _) -> key.activityHashCode == activity.hashCode() }
            .forEach { (key, activityResultLauncher) ->
                activityResultLauncher.unregister()
                actionResultActionMap.remove(key)
            }
    }

    fun get(key: RegistryKey): ActivityResultLauncher<Intent> =
        actionResultActionMap.getOrElse(key) { throw NoActionRegisteredForGivenTokenException() }

    fun <T : Any> registerResultAction(
        resultClass: KClass<T>,
        action: (T) -> Unit
    ): ResultToken {

        val token = ResultTokenGenerator.generateToken()
        if (ActivityStackContainer.isEmpty().not()) {
            // tries to create an action result launcher using the current activity
            // since the action may be registered before or after activity creation.
            val currentActivity = ActivityStackContainer.peek()
            actionResultActionMap[RegistryKey(currentActivity.hashCode(), token)] =
                ActivityResultLauncherFactory.create(resultClass, currentActivity, action)
        }

        // always registers a callback which creates an ActivityResultLauncher for
        // the current activity. This is executed lazily on activity creation callback.
        registerNewResultCallback(token, resultClass, action)

        return token
    }

    private fun <T : Any> registerNewResultCallback(
        token: ResultToken,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        ResultActionCallbackRegistry.put { activity ->
            actionResultActionMap[RegistryKey(activity.hashCode(), token)] =
                ActivityResultLauncherFactory.create(resultClass, activity, action)
        }
    }
}

class NoActionRegisteredForGivenTokenException : RuntimeException()

data class RegistryKey(val activityHashCode: Int, val token: ResultToken)