package com.discoveryone.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.navigation.AndroidScene

val Fragment.scene: AndroidScene
    get() = AndroidScene(
        componentType = AndroidScene.ComponentType.FRAGMENT,
        instanceHashCode = hashCode()
    )

val FragmentActivity.scene: AndroidScene
    get() = AndroidScene(
        componentType = AndroidScene.ComponentType.ACTIVITY,
        instanceHashCode = hashCode()
    )

val DialogFragment.scene: AndroidScene
    get() = AndroidScene(
        componentType = AndroidScene.ComponentType.DIALOG_FRAGMENT,
        instanceHashCode = hashCode()
    )