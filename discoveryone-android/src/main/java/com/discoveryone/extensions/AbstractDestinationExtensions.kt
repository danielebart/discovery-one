package com.discoveryone.extensions

import com.discoveryone.annotations.InternalDestinationArgumentMarker
import com.discoveryone.destinations.AbstractDestination
import kotlin.reflect.full.memberProperties

internal fun AbstractDestination.extractArgumentsFromDestination(): List<Pair<String, Any?>> =
    this::class.memberProperties.filter { property ->
        property.annotations.map { annotation -> annotation.annotationClass }
            .contains(InternalDestinationArgumentMarker::class)
    }
        .map { property ->
            Pair(property.name, property.getter.call(this))
        }