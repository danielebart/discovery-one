package com.discoveryone.result

import androidx.fragment.app.FragmentActivity
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

typealias ResultActionCallback = (FragmentActivity) -> Unit

object ResultActionCallbackRegistry {

    private val resultRegistry: Queue<ResultActionCallback> = ConcurrentLinkedQueue()

    fun put(callback: ResultActionCallback) {
        resultRegistry.add(callback)
    }

    fun executePendingCallbacks(activity: FragmentActivity) {
        resultRegistry.forEach { action -> action(activity) }
        resultRegistry.clear()
    }
}