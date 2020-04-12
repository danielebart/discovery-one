package com.discoveryone.sample.activity2

import com.discoveryone.Navigator
import com.discoveryone.sample.fragment1.DESTINATION_1

object Presenter2 {

    fun onNavigateToFragment1Click() {
        Navigator.navigate(DESTINATION_1)
    }
}