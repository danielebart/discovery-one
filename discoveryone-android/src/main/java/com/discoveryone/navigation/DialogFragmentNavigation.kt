package com.discoveryone.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.discoveryone.exceptions.FragmentNotFoundOnResultRegistration
import com.discoveryone.extensions.extractPropertiesForBundle
import com.discoveryone.extensions.firstFragmentOrNull
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.navigation.result.ActionLauncher
import com.discoveryone.navigation.result.ActionLauncher.launchActionOnResult
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal object DialogFragmentNavigation {

    fun navigate(currentActivity: FragmentActivity, route: GeneratedDialogFragmentRoute) {
        val dialogClass = route.clazz as KClass<DialogFragment>
        val dialogInstance = dialogClass.createInstance().apply {
            arguments = bundleOf(*route.extractPropertiesForBundle().toTypedArray())
        }

        dialogInstance.show(currentActivity.supportFragmentManager, route.clazz.qualifiedName)
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

        dialogInstance.show(currentActivity.supportFragmentManager, null)
    }

    fun close(navigationContext: NavigationContext, currentActivity: FragmentActivity) {
        val dialogFragment =
            currentActivity.firstFragmentOrNull { it.hashCode() == navigationContext.instanceHashCode } as DialogFragment
        dialogFragment.dismiss()
    }

    fun <T> closeWithResult(
        navigationContext: NavigationContext,
        currentActivity: FragmentActivity,
        result: T
    ) {
        val dialogFragment =
            currentActivity.firstFragmentOrNull { it.hashCode() == navigationContext.instanceHashCode } as DialogFragment
        val key = dialogFragment.resultKey ?: run {
            close(navigationContext, currentActivity)
            return
        }
        val bundleResult = bundleOf(ActionLauncher.DEFAULT_INTENT_EXTRA_KEY to result)
        dialogFragment.setFragmentResult(key, bundleResult)
        dialogFragment.dismiss()
    }

    fun <T : Any> registerResultAction(
        navigationContext: NavigationContext,
        key: String,
        resultClass: KClass<T>,
        action: (T) -> Unit
    ) {
        if (ActivityStackContainer.isEmpty().not()) {
            val currentActivity = ActivityStackContainer.peek()
            val fragment =
                currentActivity.firstFragmentOrNull { it.hashCode() == navigationContext.instanceHashCode }
                    ?: throw FragmentNotFoundOnResultRegistration()

            fragment.setFragmentResultListener(key) { _, bundle ->
                launchActionOnResult(bundle, resultClass, action)
            }
        } else {
            throw FragmentNotFoundOnResultRegistration()
        }
    }

    private val Fragment.resultKey: String?
        get() = arguments?.getString(DIALOG_NAVIGATION_FOR_RESULT_KEY)

    private const val DIALOG_NAVIGATION_FOR_RESULT_KEY = "DIALOG_NAVIGATION_FOR_RESULT_KEY"
}