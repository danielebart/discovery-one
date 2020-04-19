package com.discoveryone.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ActivityNavigationDestination(
    val name: String = "",
    vararg val arguments: DestinationArgument
)