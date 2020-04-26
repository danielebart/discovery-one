package com.discoveryone.sample.activity3

import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.Navigator
import com.discoveryone.registerResult
import com.discoveryone.sample.R
import com.discoveryone.sample.activity4.ACTIVITY_4
import kotlinx.android.parcel.Parcelize

class Activity3 : AppCompatActivity() {

    private val presenter = Presenter3(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_3)

        findViewById<Button>(R.id.navigateToActivity4Button).setOnClickListener {
            presenter.onBtnClick()
        }
    }

    fun showResult(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}

class Presenter3(private val view: Activity3) {

    private val token1 = Navigator.registerResult<String> {
        view.showResult(it)
    }

    private val token2 = Navigator.registerResult<TestParcelable> {
        view.showResult(it.foo.toString())
    }

    fun onBtnClick() {
        Navigator.navigateForResult(ACTIVITY_4, token1)
    }
}

@Parcelize
data class TestParcelable(val foo: Int): Parcelable