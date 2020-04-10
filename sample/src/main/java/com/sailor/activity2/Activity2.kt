package com.sailor.activity2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sailor.R

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        Presenter2.start()
    }
}