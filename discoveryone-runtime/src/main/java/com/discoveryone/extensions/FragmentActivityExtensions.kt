package com.discoveryone.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.navigation.NavigationContext

internal fun NavigationContext.retrieveRelativeFragment(activity: FragmentActivity): Fragment? =
    activity.supportFragmentManager.fragments.firstOrNull { it.hashCode() == instanceHashCode }
