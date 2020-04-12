package com.sailor.activity2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sailor.R
import com.sailor.destination.ActivityNavigationDestination

@ActivityNavigationDestination("DESTINATION_2")
class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        findViewById<Button>(R.id.navigateToFragment1Button).setOnClickListener {
            Presenter2.onNavigateToFragment1Click()
        }
    }
}