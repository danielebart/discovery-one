package com.discoveryone.initialization

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import java.util.Stack

internal object ActivityStackContainer {

    private val stack: Stack<FragmentActivity> = Stack()

    fun push(activity: FragmentActivity) {
        stack.push(activity)
    }

    fun peek(): FragmentActivity =
        stack.peek()

    fun remove(activity: Activity) =
        stack.remove(activity)

    fun isEmpty(): Boolean =
        stack.isEmpty()

    fun size(): Int =
        stack.size

    @VisibleForTesting
    fun getByName(name: String): FragmentActivity =
        stack.first { it::class.simpleName == name }

    fun getByHashCode(hashCode: Int): FragmentActivity =
        stack.first { it.hashCode() == hashCode }
}