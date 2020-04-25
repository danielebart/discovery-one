package com.discoveryone

inline fun <reified T : Any> AndroidNavigator.registerResult(noinline action: (T) -> Unit): ResultToken =
    registerResult(T::class, action)