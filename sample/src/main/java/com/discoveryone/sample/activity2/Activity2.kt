package com.discoveryone.sample.activity2

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.destination.ActivityNavigationDestination
import com.discoveryone.destination.DestinationArgument
import com.discoveryone.sample.R

@ActivityNavigationDestination(
    name = "DESTINATION_2",
    arguments = [DestinationArgument("arg1", String::class)]
)
class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        findViewById<Button>(R.id.navigateToFragment1Button).setOnClickListener {
            Presenter2.onNavigateToFragment1Click()
        }
        findViewById<View>(R.id.backButton).setOnClickListener {
            Presenter2.onNavigateBack()
        }
    }
}