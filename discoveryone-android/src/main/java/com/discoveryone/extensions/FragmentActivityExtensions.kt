package com.discoveryone.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

internal fun FragmentActivity.firstFragmentOrNull(predicate: (Fragment) -> Boolean): Fragment? =
    supportFragmentManager.fragments.firstOrNull(predicate)