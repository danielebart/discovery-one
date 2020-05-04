package com.discoveryone.sample.foodmenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.extensions.scene
import com.discoveryone.sample.R
import kotlinx.android.synthetic.main.fragment_foodmenu_selection.*

@FragmentRoute(
    name = "FoodMenuSelection",
    containerId = R.id.foodMenuContainer,
    arguments = [RouteArgument("numberOfCustomers", Int::class)]
)
class FoodMenuSelectionFragment : Fragment(R.layout.fragment_foodmenu_selection) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        numberOfCustomersTextView.text = getString(
            R.string.number_of_customers,
            numberOfCustomers
        )

        cheeseburgerButton.setOnClickListener {
            scene.closeWithResult(cheeseburgerButton.text)
        }

        sushiButton.setOnClickListener {
            scene.closeWithResult(sushiButton.text)
        }

        pizzaButton.setOnClickListener {
            scene.closeWithResult(pizzaButton.text)
        }
    }
}
