@file:Suppress("UNCHECKED_CAST")

package com.discoveryone.navigation.result

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.discoveryone.navigation.result.ActionLauncher.launchActionOnResult
import kotlin.reflect.KClass

object ActivityResultLauncherFactory {

    fun <T : Any> create(
        resultClass: KClass<T>,
        currentActivity: FragmentActivity,
        action: (T) -> Unit
    ): ActivityResultLauncher<Intent> =
        currentActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.extras?.launchActionOnResult(resultClass, action)
        }
}