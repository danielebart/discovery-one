package com.discoveryone

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import java.util.Stack

object ActivityStackContainer {

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
}