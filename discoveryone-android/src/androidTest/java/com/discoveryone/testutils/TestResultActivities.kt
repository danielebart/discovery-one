package com.discoveryone.testutils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.Navigator
import com.discoveryone.ResultToken
import com.discoveryone.annotations.ActivityNavigationDestination
import com.discoveryone.annotations.DestinationArgument
import com.discoveryone.registerResult
import com.discoveryone.result.ActivityResultLauncherFactory.DEFAULT_INTENT_EXTRA_KEY

class TestActivityWithResultRegisterBeforeOnCreated : AppCompatActivity() {

    private val resultToken = Navigator.registerResult<String> {}

    fun navigateToActivityReturningResult(valueWhichNextActivityShouldReturn: String) {
        Navigator.navigateForResult(
            TestReturningValueActivityDestination(valueWhichNextActivityShouldReturn),
            resultToken
        )
    }
}

class TestActivityWithResultRegisterAfterOnCreated : AppCompatActivity() {

    private lateinit var resultToken: ResultToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultToken = Navigator.registerResult<String> {}
    }

    fun navigateToActivityReturningResult(valueWhichNextActivityShouldReturn: String) {
        Navigator.navigateForResult(
            TestReturningValueActivityDestination(valueWhichNextActivityShouldReturn),
            resultToken
        )
    }
}

@ActivityNavigationDestination(
    arguments = [DestinationArgument("expectedReturningValue", String::class)]
)
class TestReturningValueActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val expectedReturningValue = intent.getStringExtra("expectedReturningValue")

        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(DEFAULT_INTENT_EXTRA_KEY, expectedReturningValue)
        )
        finish()
    }
}

/////////////////////////////////

class TestActivityWithWrongResultType : AppCompatActivity() {

    private val resultToken = Navigator.registerResult<String> {}

    fun navigateToActivityReturningResult() {
        Navigator.navigateForResult(
            TestReturningDifferentValueTypeActivityDestination,
            resultToken
        )
    }
}

@ActivityNavigationDestination
class TestReturningDifferentValueTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val differentValueTypeThanExpected = 500

        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(DEFAULT_INTENT_EXTRA_KEY, differentValueTypeThanExpected)
        )
        finish()
    }
}

/////////////////////////////////

class TestReturningValueSequence1Activity : AppCompatActivity() {

    private val resultToken = Navigator.registerResult<String> {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Navigator.navigateForResult(
            TestReturningValueSequence2ActivityDestination,
            resultToken
        )
    }
}

@ActivityNavigationDestination
class TestReturningValueSequence2Activity : AppCompatActivity() {

    private val resultToken = Navigator.registerResult<String> {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(DEFAULT_INTENT_EXTRA_KEY, "arg_from_activity_2")
        )
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Navigator.navigateForResult(
            TestReturningValueSequence3ActivityDestination,
            resultToken
        )
    }
}

@ActivityNavigationDestination
class TestReturningValueSequence3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(DEFAULT_INTENT_EXTRA_KEY, "arg_from_activity_3")
        )
        finish()
    }
}
