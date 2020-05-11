package com.discoveryone.sample.foodmenu

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.discoveryone.annotations.DialogFragmentRoute
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.extensions.scene
import kotlinx.android.parcel.Parcelize

@DialogFragmentRoute(
    name = "ConfirmDialog",
    arguments = [RouteArgument("order", String::class)]
)
class ConfirmDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireActivity()).setMessage("Are you sure you wanna order $order?")
            .setPositiveButton("Confirm") { _, _ ->
                scene.closeWithResult(ConfirmDialogResult.Confirm(order))
            }
            .setNegativeButton("Cancel") { _, _ ->
                scene.closeWithResult(ConfirmDialogResult.Cancel(order))
            }
            .create()

    sealed class ConfirmDialogResult : Parcelable {
        @Parcelize
        data class Confirm(val order: String) : ConfirmDialogResult()

        @Parcelize
        data class Cancel(val order: String) : ConfirmDialogResult()
    }
}