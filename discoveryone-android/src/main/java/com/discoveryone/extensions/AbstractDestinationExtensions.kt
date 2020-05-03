package com.discoveryone.extensions

import com.discoveryone.annotations.InternalRouteArgumentMarker
import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.full.memberProperties

internal fun AbstractRoute.extractArgumentsFromRoute(): List<Pair<String, Any?>> =
    this::class.memberProperties.filter { property ->
        property.annotations.map { annotation -> annotation.annotationClass }
            .contains(InternalRouteArgumentMarker::class)
    }
        .map { property ->
            Pair(property.name, property.getter.call(this))
        }