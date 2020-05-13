package com.discoveryone.testutils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.annotations.ActivityRoute
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.extensions.navigator
import com.discoveryone.extensions.onResult

@ActivityRoute
class ContainerTestActivity : AppCompatActivity(com.discoveryone.test.R.layout.container_layout)

@ActivityRoute
class EmptyTestActivity : AppCompatActivity(com.discoveryone.test.R.layout.empty_layout)

@ActivityRoute
class ListenForStringResultTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.onResult<String, ReturnStringValueTestActivityRoute> {}
    }

    fun navigateToActivityReturningResult(valueWhichNextActivityShouldReturn: String) {
        navigator.navigateForResult(
            ReturnStringValueTestActivityRoute(valueWhichNextActivityShouldReturn)
        )
    }
}

@ActivityRoute(
    arguments = [RouteArgument("expectedReturningValue", String::class)]
)
class ReturnStringValueTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.closeWithResult(expectedReturningValue)
    }
}

class ListenForStringResultTestButReceiverWrongResultTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.onResult<String, ReturnIntValueTestActivityRoute> {}
    }

    fun navigateToActivityReturningWrongResultType() {
        navigator.navigateForResult(ReturnIntValueTestActivityRoute)
    }
}

@ActivityRoute
class ReturnIntValueTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.closeWithResult(4599)
    }
}


//////////////////////////////////////////////////////////////////

class ReturningValueSequence1TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.onResult<String, ReturningValueSequence2TestActivityRoute> {}
    }

    fun navigateToActivity2() {
        navigator.navigateForResult(ReturningValueSequence2TestActivityRoute)
    }
}

@ActivityRoute
class ReturningValueSequence2TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.onResult<String, ReturningValueSequence3TestActivityRoute> {
            navigator.closeWithResult("arg_from_activity_2")
        }
    }

    fun navigateToActivity3() {
        navigator.navigateForResult(ReturningValueSequence3TestActivityRoute)
    }
}

@ActivityRoute
class ReturningValueSequence3TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.closeWithResult("arg_from_activity_3")
    }
}
