package com.discoveryone.sample.activity4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.result.ActivityResultLauncherFactory.DEFAULT_INTENT_EXTRA_KEY
import com.discoveryone.annotations.ActivityNavigationDestination
import com.discoveryone.sample.R

@ActivityNavigationDestination(name = "ACTIVITY_4")
class Activity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        findViewById<Button>(R.id.navigateToActivity4Button).setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra(DEFAULT_INTENT_EXTRA_KEY, "pippo"))
            finish()
        }
    }
}