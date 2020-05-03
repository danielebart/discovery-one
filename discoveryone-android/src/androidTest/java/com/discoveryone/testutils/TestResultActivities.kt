package com.discoveryone.testutils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.annotations.ActivityNavigationDestination
import com.discoveryone.annotations.DestinationArgument
import com.discoveryone.extensions.onResult
import com.discoveryone.extensions.scene

class ListenForStringResultTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scene.onResult<String>("key_result") {}
    }

    fun navigateToActivityReturningResult(valueWhichNextActivityShouldReturn: String) {
        scene.navigateForResult(
            "key_result",
            ReturnStringValueTestActivityDestination(valueWhichNextActivityShouldReturn)
        )
    }
}

@ActivityNavigationDestination(
    arguments = [DestinationArgument("expectedReturningValue", String::class)]
)
class ReturnStringValueTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val expectedReturningValue = intent.getStringExtra("expectedReturningValue")
        scene.close(expectedReturningValue)
    }
}

class ListenForStringResultTestButReceiverWrongResultTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scene.onResult<String>("key_result") {}
    }

    fun navigateToActivityReturningWrongResultType() {
        scene.navigateForResult("key_result", ReturnIntValueTestActivityDestination)
    }
}

@ActivityNavigationDestination
class ReturnIntValueTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scene.close(4599)
    }
}


//////////////////////////////////////////////////////////////////

class ReturningValueSequence1TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scene.onResult<String>("key_result") {}
    }

    fun navigateToActivity2() {
        scene.navigateForResult("key_result", ReturningValueSequence2TestActivityDestination)
        waitForIdleSync()
    }
}

@ActivityNavigationDestination
class ReturningValueSequence2TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scene.onResult<String>("key_result") {
            scene.close("arg_from_activity_2")
        }
    }

    fun navigateToActivity3() {
        scene.navigateForResult("key_result", ReturningValueSequence3TestActivityDestination)
        waitForIdleSync()
    }
}

@ActivityNavigationDestination
class ReturningValueSequence3TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scene.close("arg_from_activity_3")
    }
}
