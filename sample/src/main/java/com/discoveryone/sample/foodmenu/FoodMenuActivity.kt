package com.discoveryone.sample.foodmenu

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.discoveryone.extensions.navigator
import com.discoveryone.sample.R

class FoodMenuActivity : FragmentActivity(R.layout.activity_foodmenu) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.navigate(FoodMenuHome)
        }
    }
}
