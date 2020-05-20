package com.discoveryone.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import com.discoveryone.extensions.extractPropertiesForBundle
import com.discoveryone.extensions.retrieveRelativeFragment
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
        val dialogFragment =
            navigationContext.retrieveRelativeFragment(currentActivity) as DialogFragment
        dialogFragment.dismiss()
    }

    fun <T> closeWithResult(
        navigationContext: NavigationContext,
        currentActivity: FragmentActivity,
        result: T
    ) {
        val dialogFragment =
            navigationContext.retrieveRelativeFragment(currentActivity) as DialogFragment
        val key = dialogFragment.resultKey ?: run {
            close(navigationContext, currentActivity)
            return
        }
        val bundleResult = bundleOf(DEFAULT_INTENT_EXTRA_KEY to result)
        dialogFragment.setFragmentResult(key, bundleResult)
        dialogFragment.dismiss()
    }

    private val Fragment.resultKey: String?
        get() = arguments?.getString(DIALOG_NAVIGATION_FOR_RESULT_KEY)

    private const val DIALOG_NAVIGATION_FOR_RESULT_KEY = "DIALOG_NAVIGATION_FOR_RESULT_KEY"
}