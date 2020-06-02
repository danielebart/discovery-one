package com.discoveryone.sample.foodmenu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.discoveryone.Navigator
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

    private val presenter by lazy { FoodMenuSelectionPresenter(navigator) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.start()

        presenter.displayToastLiveData.observe(viewLifecycleOwner, Observer { text ->
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        })

        numberOfCustomersTextView.text = getString(
            R.string.number_of_customers,
            numberOfCustomers
        )

        cheeseburgerButton.setOnClickListener {
            presenter.onFoodButtonClick(cheeseburgerButton.text.toString())
        }

        sushiButton.setOnClickListener {
            presenter.onFoodButtonClick(sushiButton.text.toString())
        }

        pizzaButton.setOnClickListener {
            presenter.onFoodButtonClick(pizzaButton.text.toString())
        }
    }
}

class FoodMenuSelectionPresenter(private val navigator: Navigator) {

    val displayToastLiveData: MutableLiveData<String> = MutableLiveData()

    fun start() {
        navigator.onResult<ConfirmDialogResult, ConfirmDialog> { result ->
            when (result) {
                is ConfirmDialogResult.Confirm -> navigator.closeWithResult(result.order)
                is ConfirmDialogResult.Cancel -> displayToastLiveData.postValue("order canceled")
            }
        }
    }

    fun onFoodButtonClick(order: String) {
        navigator.navigateForResult(ConfirmDialog(order))
    }
}
