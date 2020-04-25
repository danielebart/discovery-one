package com.discoveryone

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.discoveryone.destinations.ActivityDestination

object ActivityNavigation {

    internal fun navigate(currentActivity: FragmentActivity, destination: ActivityDestination) {
        val arguments = destination.extractArgumentsFromDestination().toTypedArray()
        val intent = Intent(currentActivity, destination.clazz.java).putExtras(bundleOf(*arguments))

        currentActivity.startActivity(intent)
    }

    internal fun <T> navigateForResult(
        currentActivity: FragmentActivity,
        destination: ActivityDestination,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        val arguments = destination.extractArgumentsFromDestination().toTypedArray()
        val intent = Intent(currentActivity, destination.clazz.java).putExtras(bundleOf(*arguments))
        activityResultLauncher.launch(intent)
    }
}