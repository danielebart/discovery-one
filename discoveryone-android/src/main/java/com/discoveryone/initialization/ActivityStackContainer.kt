package com.discoveryone.initialization

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import com.discoveryone.exceptions.NoActivityOnStack
import java.util.Deque
import java.util.concurrent.LinkedBlockingDeque

internal object ActivityStackContainer {

    private val deque: Deque<FragmentActivity> = LinkedBlockingDeque()

    fun push(activity: FragmentActivity) {
        deque.push(activity)
    }

    fun peek(): FragmentActivity =
        deque.peek() ?: throw NoActivityOnStack()

    fun remove(activity: Activity) =
        deque.remove(activity)

    fun isEmpty(): Boolean =
        deque.isEmpty()

    fun size(): Int =
        deque.size

    @VisibleForTesting
    fun getByName(name: String): FragmentActivity =
        deque.first { it::class.simpleName == name }

    @VisibleForTesting
    fun clear() {
        deque.clear()
    }

    fun getByHashCode(hashCode: Int): FragmentActivity =
        deque.first { it.hashCode() == hashCode }
}