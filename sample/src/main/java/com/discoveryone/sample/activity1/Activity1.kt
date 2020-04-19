package com.discoveryone.sample.activity1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.sample.R

class Activity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        findViewById<Button>(R.id.navigateToActivity4Button).setOnClickListener {
            Presenter1.onButton1Click()
        }
    }
}