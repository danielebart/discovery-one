package com.discoveryone.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.navigation.ActivityNavigation.ACTIVITY_TAG_KEY
import com.discoveryone.navigation.AndroidNavigator
import com.discoveryone.navigation.NavigationContext

val Fragment.navigator: Navigator
    get() = AndroidNavigator(
        NavigationContext(
            componentType = NavigationContext.ComponentType.FRAGMENT,
            instanceHashCode = hashCode(),
            componentClass = this::class
        )
    )

val FragmentActivity.navigator: Navigator
    get() = AndroidNavigator(
        NavigationContext(
            componentType = NavigationContext.ComponentType.ACTIVITY,
            instanceHashCode = hashCode(),
            componentClass = this::class,
            extra = intent.getStringExtra(ACTIVITY_TAG_KEY)
        )
    )

val DialogFragment.navigator: Navigator
    get() = AndroidNavigator(
        NavigationContext(
            componentType = NavigationContext.ComponentType.DIALOG_FRAGMENT,
            instanceHashCode = hashCode(),
            componentClass = this::class
        )
    )