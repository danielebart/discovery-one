package com.sailor

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.Stack

object Navigator {

    val stack = Stack<AppCompatActivity>()

    inline fun <reified A: AppCompatActivity> navigateToActivity() {
        val currentActivity = stack.peek()
        currentActivity.startActivity(Intent(currentActivity, A::class.java))
    }

    inline fun <reified F: Fragment> navigateToFragment(@IdRes containerId: Int) {
        val currentActivity = stack.peek()
        currentActivity.supportFragmentManager.beginTransaction()
            .replace(containerId, F::class.java.newInstance())
            .commit()
    }
}