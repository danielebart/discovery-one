package com.discoveryone.navigation.result

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import com.discoveryone.exceptions.FragmentNotFoundOnResultRegistration
import com.discoveryone.exceptions.NoActionRegisteredForGivenKeyException
import com.discoveryone.extensions.retrieveRelativeFragment
import com.discoveryone.initialization.ActivityInterceptor
import com.discoveryone.navigation.NavigationContext
import com.discoveryone.routes.AbstractRoute
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import com.discoveryone.routes.GeneratedFragmentRoute
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

internal object ResultRegistry {

    private val activityResultLauncherMap: MutableMap<ActivityResultLauncherMapKey, ActivityResultLauncher<Intent>> =
        mutableMapOf()

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
                action = action
            )
        } else if (routeClass.isFragmentOrDialogFragment()) {
            registerResultActionForFragmentRoute(
                navigationContext = navigationContext,
                routeClass = routeClass,
                resultClass = resultClass,
                action = action
            )
        }
    }

    private fun <T : Any, R : AbstractRoute> registerResultActionForActivityRoute(
        navigationContext: NavigationContext,
        routeClass: KClass<R>,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        val currentActivity = ActivityInterceptor.getLast()
        val key = ActivityResultLauncherMapKey(
            activityInstanceHashCode = navigationContext.instanceHashCode, // from
            key = routeClass.qualifiedName.toString() // to
        )
        activityResultLauncherMap[key] =
            createActivityResultLauncher(resultClass, currentActivity, action)
    }

    private fun <T : Any, R : AbstractRoute> registerResultActionForFragmentRoute(
        navigationContext: NavigationContext,
        routeClass: KClass<R>,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        val currentActivity = ActivityInterceptor.getLast()
        when (navigationContext.componentType) {
            NavigationContext.ComponentType.ACTIVITY -> {
                currentActivity.supportFragmentManager.setFragmentResultListener(
                    requestKey = buildSimpleResultKey(routeClass),
                    lifecycleOwner = currentActivity
                ) { _, bundle ->
                    ActionLauncher.launchActionOnResult(bundle, resultClass, action)
                }
            }
            NavigationContext.ComponentType.DIALOG_FRAGMENT, NavigationContext.ComponentType.FRAGMENT -> {
                val fragment = navigationContext.retrieveRelativeFragment(currentActivity)
                    ?: throw FragmentNotFoundOnResultRegistration()
                fragment.setFragmentResultListener(buildSimpleResultKey(routeClass)) { _, bundle ->
                    ActionLauncher.launchActionOnResult(bundle, resultClass, action)
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
                ActionLauncher.launchActionOnResult(bundle, resultClass, action)
            }
        }

    private fun <R : AbstractRoute> KClass<R>.isFragmentOrDialogFragment(): Boolean =
        isSubclassOf(GeneratedDialogFragmentRoute::class) || isSubclassOf(GeneratedFragmentRoute::class)

    internal fun <R : AbstractRoute> buildSimpleResultKey(routeClass: KClass<R>): String =
        routeClass.qualifiedName.toString()

    private data class ActivityResultLauncherMapKey(
        val activityInstanceHashCode: Int,
        val key: String
    )
}