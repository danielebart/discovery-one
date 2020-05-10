package com.discoveryone.testutils

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.discoveryone.annotations.ActivityRoute
import com.discoveryone.annotations.DialogFragmentRoute
import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.extensions.onResult
import com.discoveryone.extensions.scene

@DialogFragmentRoute
class TestDialogFragment : DialogFragment(com.discoveryone.test.R.layout.empty_layout)

@DialogFragmentRoute
class TestDialogFragment2 : DialogFragment(com.discoveryone.test.R.layout.empty_layout)

@DialogFragmentRoute
class ListenForStringResultTestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result") {}
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextDialogFragmentShouldReturn: String) {
        scene.navigateForResult(
            "key_result",
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextDialogFragmentShouldReturn)
        )
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResultFromDialogTestFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result") { }
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextDialogFragmentShouldReturn: String) {
        scene.navigateForResult(
            "key_result",
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextDialogFragmentShouldReturn)
        )
    }
}

@DialogFragmentRoute(
    arguments = [RouteArgument("expectedReturningValue", String::class)]
)
class ReturnStringValueTestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        val expectedReturningValue = requireArguments().getString("expectedReturningValue")
        scene.closeWithResult(expectedReturningValue)
    }
}

//////////////////////////////////////////////////////////////////

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResulButReceiverWrongResultTypeFromDialogTestFragment :
    Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result") {}
    }

    fun navigateToDialogFragmentReturningWrongResultType() {
        scene.navigateForResult("key_result", ReturnIntValueTestDialogFragmentRoute)
    }
}

@DialogFragmentRoute
class ReturnIntValueTestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        scene.closeWithResult(1231)
    }
}

//////////////////////////////////////////////////////////////////
@DialogFragmentRoute
class ReturningValueSequence1TestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result") {
            println("")
        }
    }

    fun navigateToDialogFragment2() {
        scene.navigateForResult("key_result", ReturningValueSequenceOfDialogsTestFragmentRoute)
        waitForIdleSync()
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequenceOfDialogsTestFragment :
    Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result2") {
            scene.closeWithResult("arg_from_DialogFragment_2")
        }
    }

    fun navigateToDialogFragment3() {
        scene.navigateForResult("key_result2", ReturningValueSequence3TestDialogFragmentRoute)
        waitForIdleSync()
    }
}

@DialogFragmentRoute
class ReturningValueSequence3TestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        scene.closeWithResult("arg_from_DialogFragment_3")
    }
}
//////////////////////////////////////////////////////////////////

@ActivityRoute
class ListenForStringResultFromDialogTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scene.onResult<String>("key_result") {
            println("")
        }
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextActivityShouldReturn: String) {
        scene.navigateForResult(
            "key_result",
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextActivityShouldReturn)
        )
    }
}