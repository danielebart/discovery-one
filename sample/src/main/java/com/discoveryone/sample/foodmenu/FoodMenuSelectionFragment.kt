package com.discoveryone.sample.foodmenu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.extensions.navigator
import com.discoveryone.extensions.onResult
import com.discoveryone.sample.R
import com.discoveryone.sample.foodmenu.ConfirmDialogFragment.ConfirmDialogResult
import kotlinx.android.synthetic.main.fragment_foodmenu_selection.*

@FragmentRoute(
    name = "FoodMenuSelection",
    containerId = R.id.foodMenuContainer,
    arguments = [RouteArgument("numberOfCustomers", Int::class)]
)
class FoodMenuSelectionFragment : Fragment(R.layout.fragment_foodmenu_selection) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navigator.onResult<ConfirmDialogResult, ConfirmDialog> { result ->
            when (result) {
                is ConfirmDialogResult.Confirm -> navigator.closeWithResult(result.order)
                is ConfirmDialogResult.Cancel ->
                    Toast.makeText(requireContext(), "order canceled", Toast.LENGTH_SHORT).show()
            }
        }

        numberOfCustomersTextView.text = getString(
            R.string.number_of_customers,
            numberOfCustomers
        )

        cheeseburgerButton.setOnClickListener {
            openConfirmDialog(cheeseburgerButton.text.toString())
        }

        sushiButton.setOnClickListener {
            openConfirmDialog(sushiButton.text.toString())
        }

        pizzaButton.setOnClickListener {
            openConfirmDialog(sushiButton.text.toString())
        }
    }

    private fun openConfirmDialog(order: String) {
        navigator.navigateForResult(ConfirmDialog(order))
    }
}
