package com.discoveryone.sample.fragment1

import androidx.fragment.app.Fragment
import com.discoveryone.R
import com.discoveryone.destination.FragmentNavigationDestination

@FragmentNavigationDestination("DESTINATION_1", containerId = R.id.container)
class Fragment1 : Fragment(R.layout.fragment_1) {

}