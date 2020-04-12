package com.sailor.activity1

import com.sailor.Navigator
import com.sailor.activity2.DESTINATION_2

object Presenter1 {

    fun onButton1Click() {
        Navigator.navigate(DESTINATION_2("pippo"))
    }
}