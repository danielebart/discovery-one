package com.discoveryone.navigation.result

import android.os.Bundle
import android.util.Log
import com.discoveryone.utils.DiscoveryOneLog
import kotlin.reflect.KClass

internal object ActionLauncher {

    private var resultSpy: ResultSpy? = null

    fun injectActivityResultSpy(spy: ResultSpy) {
        resultSpy = spy
    }

    fun <T : Any> Bundle.launchActionOnResult(resultClass: KClass<T>, action: (T) -> Unit) {
        val result = get(DEFAULT_INTENT_EXTRA_KEY)
        if (result != null && result::class == resultClass) {
            action(result as T)
            resultSpy?.recordResult(result)
        } else {
            Log.w(
                DiscoveryOneLog.DISCOVERY_ONE_LOG_TAG,
                "result is not an instance of the expected type"
            )
        }
    }

    internal const val DEFAULT_INTENT_EXTRA_KEY = "default_intent_extra_key"
}