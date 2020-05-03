@file:JvmName("SceneExtensions")

package com.discoveryone.extensions

import com.discoveryone.Scene

inline fun <reified T : Any> Scene.onResult(key: String, noinline action: (T) -> Unit) {
    onResult(key, T::class, action)
}
