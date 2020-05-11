package com.discoveryone.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.Navigator
import com.discoveryone.navigation.AndroidNavigator
import com.discoveryone.navigation.AndroidScene

val Fragment.navigator: Navigator
    get() = AndroidNavigator(
        AndroidScene(
            componentType = AndroidScene.ComponentType.FRAGMENT,
            instanceHashCode = hashCode(),
            componentClass = this::class
        )
    )

val FragmentActivity.navigator: Navigator
    get() = AndroidNavigator(
        AndroidScene(
            componentType = AndroidScene.ComponentType.ACTIVITY,
            instanceHashCode = hashCode(),
            componentClass = this::class
        )
    )

val DialogFragment.navigator: Navigator
    get() = AndroidNavigator(
        AndroidScene(
            componentType = AndroidScene.ComponentType.DIALOG_FRAGMENT,
            instanceHashCode = hashCode(),
            componentClass = this::class
        )
    )