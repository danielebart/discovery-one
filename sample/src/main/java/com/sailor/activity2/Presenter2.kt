package com.sailor.activity2

import com.sailor.R
import com.sailor.fragment1.Fragment1
import com.sailor.Navigator

object Presenter2 {

    fun start() {
        Navigator.navigateToFragment<Fragment1>(R.id.container)
    }
}