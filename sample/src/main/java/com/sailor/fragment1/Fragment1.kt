package com.sailor.fragment1

import androidx.fragment.app.Fragment
import com.sailor.R
import com.sailor.destination.FragmentNavigationDestination

@FragmentNavigationDestination("DESTINATION_1", containerId = R.id.container)
class Fragment1 : Fragment(R.layout.fragment_1) {

}