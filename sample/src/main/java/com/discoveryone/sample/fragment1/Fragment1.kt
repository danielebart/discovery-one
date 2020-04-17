package com.discoveryone.sample.fragment1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.discoveryone.destination.DestinationArgument
import com.discoveryone.destination.FragmentNavigationDestination
import com.discoveryone.sample.R

@FragmentNavigationDestination(
    name = "DESTINATION_1",
    containerId = R.id.container,
    arguments = [
        DestinationArgument("foo1", Int::class),
        DestinationArgument("foo2", Double::class)
    ]
)
class Fragment1 : Fragment(R.layout.fragment_1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}