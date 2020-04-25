package com.discoveryone.sample.activity1

import com.discoveryone.Navigator
import com.discoveryone.sample.activity2.DESTINATION_2

object Presenter1 {

    fun onButton1Click() {
        Navigator.navigate(DESTINATION_2("foo"))
    }
}