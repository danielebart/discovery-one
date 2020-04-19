package com.discoveryone

inline fun <reified T: Any> Navigator.registerResult(noinline action: (T) -> Unit): ResultToken =
    registerResult(T::class, action)
