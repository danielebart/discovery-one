package com.sailor.destination

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class FragmentNavigationDestination(val name: String, val containerId: Int)