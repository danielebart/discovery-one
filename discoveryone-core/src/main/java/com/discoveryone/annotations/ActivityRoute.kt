package com.discoveryone.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ActivityRoute(
    val name: String = "",
    vararg val arguments: RouteArgument
)