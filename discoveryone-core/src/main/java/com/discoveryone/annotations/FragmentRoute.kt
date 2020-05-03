package com.discoveryone.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class FragmentRoute(
    val name: String = "",
    val containerId: Int,
    vararg val arguments: RouteArgument
)