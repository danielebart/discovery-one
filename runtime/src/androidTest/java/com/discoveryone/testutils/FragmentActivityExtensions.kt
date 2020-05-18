package com.discoveryone.testutils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.getFragment(position: Int = 0): Fragment =
    supportFragmentManager.fragments[position]

inline fun <reified F : Fragment> FragmentActivity.getSpecificFragment(): F =
    supportFragmentManager.fragments.first { it is F } as F