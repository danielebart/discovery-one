package com.discoveryone.sample.foodmenu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    private val viewModel by viewModels<FoodMenuSelectionViewModel> {
        FoodMenuSelectionViewModel.factory(navigator)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.registerResults()

        viewModel.displayToastLiveData.observe(viewLifecycleOwner, Observer { text ->
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        })

        numberOfCustomersTextView.text = getString(
            R.string.number_of_customers,
            numberOfCustomers
        )

        cheeseburgerButton.setOnClickListener {
            viewModel.onFoodButtonClick(cheeseburgerButton.text.toString())
        }

        sushiButton.setOnClickListener {
            viewModel.onFoodButtonClick(sushiButton.text.toString())
        }

        pizzaButton.setOnClickListener {
            viewModel.onFoodButtonClick(pizzaButton.text.toString())
        }
    }
}

class FoodMenuSelectionViewModel(private val navigator: Navigator) : ViewModel() {

    val displayToastLiveData: MutableLiveData<String> = MutableLiveData()

    fun registerResults() {
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

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(navigator: Navigator): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    FoodMenuSelectionViewModel(navigator) as T
            }
    }
}
