package com.discoveryone.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.extensions.extractPropertiesForBundle
import com.discoveryone.navigation.result.ResultRegistry.DEFAULT_INTENT_EXTRA_KEY
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal object DialogFragmentNavigation {

    fun navigate(currentActivity: FragmentActivity, route: GeneratedDialogFragmentRoute) {
        val dialogClass = route.clazz as KClass<DialogFragment>
        val dialogInstance = dialogClass.createInstance().apply {
            arguments = bundleOf(*route.extractPropertiesForBundle().toTypedArray())
        }

        dialogInstance.show(
            currentActivity.supportFragmentManager,
            dialogInstance.hashCode().toString()
        )
    }

    fun navigateForResult(
        currentActivity: FragmentActivity,
        route: GeneratedDialogFragmentRoute,
        key: String
    ) {
        val dialogClass = route.clazz as KClass<DialogFragment>
        val dialogInstance = dialogClass.createInstance().apply {
            arguments = bundleOf(
                *route.extractPropertiesForBundle().toTypedArray(),
                DIALOG_NAVIGATION_FOR_RESULT_KEY to key
            )
        }

        dialogInstance.show(
            currentActivity.supportFragmentManager,
            dialogInstance.hashCode().toString()
        )
    }

    fun close(navigationContext: NavigationContext, currentActivity: FragmentActivity) {
        val currentDialogFragment = currentActivity.supportFragmentManager
            .fragments
            .first { it.hashCode() == navigationContext.instanceHashCode } as DialogFragment
        currentDialogFragment.dismiss()
    }

    fun <T> closeWithResult(
        navigationContext: NavigationContext,
        currentActivity: FragmentActivity,
        result: T
    ) {
        val key = navigationContext.extra ?: run {
            currentActivity.onBackPressed()
            return
        }
        val bundleResult = bundleOf(DEFAULT_INTENT_EXTRA_KEY to result)
        currentActivity.supportFragmentManager.setFragmentResult(key, bundleResult)
        close(navigationContext, currentActivity)
    }

    internal const val DIALOG_NAVIGATION_FOR_RESULT_KEY = "DIALOG_NAVIGATION_FOR_RESULT_KEY"
}