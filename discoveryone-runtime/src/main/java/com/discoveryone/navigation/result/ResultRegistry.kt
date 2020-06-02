package com.discoveryone.navigation.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import com.discoveryone.exceptions.NoActionRegisteredForGivenKeyException
import com.discoveryone.navigation.NavigationContext
import com.discoveryone.routes.AbstractRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import com.discoveryone.routes.GeneratedFragmentRoute
import com.discoveryone.utils.DiscoveryOneLog
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

internal object ResultRegistry {

    private val activityResultLauncherMap: MutableMap<ActivityResultLauncherMapKey, ActivityResultLauncher<Intent>> =
        mutableMapOf()

    private var resultSpy: ResultSpy? = null

    fun <R : AbstractRoute> executeActivityResultLauncher(
        routeClass: KClass<R>,
        navigationContext: NavigationContext,
        intent: Intent
    ) {
        val key = ActivityResultLauncherMapKey(
            navigationContext.instanceHashCode,
            buildSimpleResultKey(routeClass)
        )
        val activityResultLauncher =
            activityResultLauncherMap[key] ?: throw NoActionRegisteredForGivenKeyException()
        activityResultLauncherMap.remove(key)
        activityResultLauncher.launch(intent)
    }

    fun <T : Any, R : AbstractRoute> registerResultAction(
        currentActivity: FragmentActivity,
        navigationContext: NavigationContext,
        routeClass: KClass<R>,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        if (routeClass.isSubclassOf(GeneratedActivityRoute::class)) {
            registerResultActionForActivityRoute(
                navigationContext = navigationContext,
                routeClass = routeClass,
                resultClass = resultClass,
                action = action,
                currentActivity = currentActivity
            )
        } else if (routeClass.isFragmentOrDialogFragment()) {
            registerResultActionForFragmentRoute(
                navigationContext = navigationContext,
                routeClass = routeClass,
                resultClass = resultClass,
                action = action,
                currentActivity = currentActivity
            )
        }
    }

    private fun <T : Any, R : AbstractRoute> registerResultActionForActivityRoute(
        currentActivity: FragmentActivity,
        navigationContext: NavigationContext,
        routeClass: KClass<R>,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        val key = ActivityResultLauncherMapKey(
            activityInstanceHashCode = navigationContext.instanceHashCode, // from
            key = routeClass.qualifiedName.toString() // to
        )
        activityResultLauncherMap[key] =
            createActivityResultLauncher(resultClass, currentActivity, action)
    }

    private fun <T : Any, R : AbstractRoute> registerResultActionForFragmentRoute(
        currentActivity: FragmentActivity,
        navigationContext: NavigationContext,
        routeClass: KClass<R>,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        when (navigationContext.componentType) {
            NavigationContext.ComponentType.ACTIVITY -> {
                currentActivity.supportFragmentManager.setFragmentResultListener(
                    requestKey = buildSimpleResultKey(routeClass),
                    lifecycleOwner = currentActivity
                ) { _, bundle ->
                    launchActionOnResult(bundle, resultClass, action)
                }
            }
            NavigationContext.ComponentType.DIALOG_FRAGMENT, NavigationContext.ComponentType.FRAGMENT -> {
                currentActivity.supportFragmentManager.setFragmentResultListener(
                    requestKey = buildSimpleResultKey(routeClass),
                    lifecycleOwner = currentActivity
                ) { _, bundle ->
                    launchActionOnResult(bundle, resultClass, action)
                }
            }
        }
    }

    fun unregisterActivityResultLauncher(activity: Activity) {
        activityResultLauncherMap
            .filter { (key, _) ->
                key.activityInstanceHashCode == activity.hashCode()
            }.forEach { (key, _) ->
                activityResultLauncherMap.remove(key)
            }
    }

    private fun <T : Any> createActivityResultLauncher(
        resultClass: KClass<T>,
        currentActivity: FragmentActivity,
        action: (T) -> Unit
    ): ActivityResultLauncher<Intent> =
        currentActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            activityResult.data?.extras?.let { bundle ->
                launchActionOnResult(bundle, resultClass, action)
            }
        }

    private fun <T : Any> launchActionOnResult(
        bundle: Bundle,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        val result = bundle.get(DEFAULT_INTENT_EXTRA_KEY)
        if (result != null && resultClass.isInstance(result)) {
            resultSpy?.recordResult(result)
            action(result as T)
        } else {
            Log.w(
                DiscoveryOneLog.DISCOVERY_ONE_LOG_TAG,
                "result is not an instance of the expected type"
            )
        }
    }

    fun injectActivityResultSpy(spy: ResultSpy) {
        resultSpy = spy
    }

    private fun <R : AbstractRoute> KClass<R>.isFragmentOrDialogFragment(): Boolean =
        isSubclassOf(GeneratedDialogFragmentRoute::class) || isSubclassOf(GeneratedFragmentRoute::class)

    internal fun <R : AbstractRoute> buildSimpleResultKey(routeClass: KClass<R>): String =
        routeClass.qualifiedName.toString()

    private data class ActivityResultLauncherMapKey(
        val activityInstanceHashCode: Int,
        val key: String
    )

    const val DEFAULT_INTENT_EXTRA_KEY = "default_intent_extra_key"
}