package com.discoveryone.navigation

import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.discoveryone.extensions.extractPropertiesForBundle
import com.discoveryone.navigation.result.ResultRegistry
import com.discoveryone.navigation.result.ResultRegistry.DEFAULT_INTENT_EXTRA_KEY
import com.discoveryone.routes.GeneratedActivityRoute
import java.util.UUID

internal object ActivityNavigation {

    fun navigate(currentActivity: FragmentActivity, route: GeneratedActivityRoute) {
        val properties = route.extractPropertiesForBundle().toTypedArray()
        val intent = Intent(currentActivity, route.clazz.java).putExtras(
            bundleOf(
                *properties,
                ACTIVITY_TAG_KEY to UUID.randomUUID().toString() // TODO refactor activity tagging
            )
        )

        currentActivity.startActivity(intent)
    }

    fun navigateForResult(
        navigationContext: NavigationContext,
        currentActivity: FragmentActivity,
        route: GeneratedActivityRoute
    ) {
        val properties = route.extractPropertiesForBundle().toTypedArray()
        val intent = Intent(currentActivity, route.clazz.java).putExtras(
            bundleOf(
                *properties,
                ACTIVITY_TAG_KEY to UUID.randomUUID().toString() // TODO refactor activity tagging
            )
        )
        ResultRegistry.executeActivityResultLauncher(
            routeClass = route::class,
            navigationContext = navigationContext,
            intent = intent
        )
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

    internal const val ACTIVITY_TAG_KEY = "ACTIVITY_TAG_KEY"
}