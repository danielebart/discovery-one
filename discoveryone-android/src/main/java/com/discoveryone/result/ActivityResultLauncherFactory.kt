@file:Suppress("UNCHECKED_CAST")

package com.discoveryone.result

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.discoveryone.Log.DISCOVERY_ONE_LOG_TAG
import kotlin.reflect.KClass

object ActivityResultLauncherFactory {

    private var resultSpy: ResultSpy? = null

    fun injectActivityResultSpy(spy: ResultSpy) {
        resultSpy = spy
    }

    fun <T: Any> create(
        resultClass: KClass<T>,
        currentActivity: FragmentActivity,
        action: (T) -> Unit
    ): ActivityResultLauncher<Intent> =
        currentActivity.prepareCall(ActivityResultContracts.StartActivityForResult()) {
            val result = it.data?.extras?.get(DEFAULT_INTENT_EXTRA_KEY)
            if (result != null && result::class == resultClass) {
                action(result as T)
                resultSpy?.recordResult(result)
            } else {
                Log.w(DISCOVERY_ONE_LOG_TAG, "result is not an instance of the expected type")
            }
        }

    // TODO this must be used only internal, instead we should provide a "finish" relative API
    const val DEFAULT_INTENT_EXTRA_KEY = "default_intent_extra_key"
}