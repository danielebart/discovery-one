package com.sailor.activity1

import com.sailor.activity2.Activity2
import com.sailor.Navigator

object Presenter1 {

    fun onButton1Click() {
        Navigator.navigateToActivity<Activity2>()
    }
}