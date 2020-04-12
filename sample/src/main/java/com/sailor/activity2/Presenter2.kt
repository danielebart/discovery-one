package com.sailor.activity2

import com.sailor.Navigator
import com.sailor.fragment1.DESTINATION_1

object Presenter2 {

    fun onNavigateToFragment1Click() {
        Navigator.navigate(DESTINATION_1)
    }
}