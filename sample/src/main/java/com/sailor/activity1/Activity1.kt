package com.sailor.activity1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sailor.R
import com.sailor.destination.Destination

@Destination("DESTINATION_1")
class Activity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        findViewById<Button>(R.id.button1).setOnClickListener {
            Presenter1.onButton1Click()
        }
    }
}