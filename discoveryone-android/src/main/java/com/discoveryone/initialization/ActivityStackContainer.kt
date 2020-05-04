package com.discoveryone.initialization

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import com.discoveryone.exceptions.NoActivityOnStack
import java.util.Deque
import java.util.concurrent.LinkedBlockingDeque

internal object ActivityStackContainer {

    private val stack: Deque<FragmentActivity> = LinkedBlockingDeque()

    fun push(activity: FragmentActivity) {
        stack.push(activity)
    }

    fun peek(): FragmentActivity =
        stack.peek() ?: throw NoActivityOnStack()

    fun remove(activity: Activity) =
        stack.remove(activity)

    fun isEmpty(): Boolean =
        stack.isEmpty()

    fun size(): Int =
        stack.size

    @VisibleForTesting
    fun getByName(name: String): FragmentActivity =
        stack.first { it::class.simpleName == name }

    @VisibleForTesting
    fun clear() {
        stack.clear()
    }

    fun getByHashCode(hashCode: Int): FragmentActivity =
        stack.first { it.hashCode() == hashCode }
}