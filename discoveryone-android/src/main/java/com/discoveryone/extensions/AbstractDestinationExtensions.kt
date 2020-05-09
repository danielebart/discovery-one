package com.discoveryone.extensions

import android.util.Log
import com.discoveryone.bundle.canBeAddedInBundle
import com.discoveryone.routes.AbstractRoute
import kotlin.reflect.full.memberProperties

internal fun AbstractRoute.extractPropertiesForBundle(): List<Pair<String, Any?>> {
    return this::class.memberProperties
        .map { property ->
            Pair(property.name, property.getter.call(this))
        }
        .filter { (_, value) -> value.canBeAddedInBundle() }
}