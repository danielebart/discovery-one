@file:JvmName("SceneExtensions")

package com.discoveryone.extensions

import com.discoveryone.Navigator

inline fun <reified T : Any> Navigator.onResult(key: String, noinline action: (T) -> Unit) {
    onResult(key, T::class, action)
}
