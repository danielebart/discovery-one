package com.discoveryone.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.navigation.AndroidNavigator
import com.discoveryone.navigation.DialogFragmentNavigation
import com.discoveryone.navigation.FragmentNavigation
import com.discoveryone.navigation.NavigationContext

val Fragment.navigator: Navigator
    get() = AndroidNavigator(
        activity = requireActivity(),
        navigationContext = NavigationContext(
            componentType = NavigationContext.ComponentType.FRAGMENT,
            instanceHashCode = hashCode(),
            componentClass = this::class,
            extra = arguments?.getString(FragmentNavigation.FRAGMENT_NAVIGATION_FOR_RESULT_KEY)
        )
    )

val FragmentActivity.navigator: Navigator
    get() = AndroidNavigator(
        activity = this,
        navigationContext = NavigationContext(
            componentType = NavigationContext.ComponentType.ACTIVITY,
            instanceHashCode = hashCode(),
            componentClass = this::class
        )
    )

val DialogFragment.navigator: Navigator
    get() = AndroidNavigator(
        activity = requireActivity(),
        navigationContext = NavigationContext(
            componentType = NavigationContext.ComponentType.DIALOG_FRAGMENT,
            instanceHashCode = hashCode(),
            componentClass = this::class,
            extra = arguments?.getString(DialogFragmentNavigation.DIALOG_NAVIGATION_FOR_RESULT_KEY)
        )
    )