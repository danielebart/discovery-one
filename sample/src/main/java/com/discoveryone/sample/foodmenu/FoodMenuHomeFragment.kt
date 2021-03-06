package com.discoveryone.sample.foodmenu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.discoveryone.Navigator
import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.extensions.navigator
import com.discoveryone.extensions.onResult
import com.discoveryone.sample.R
import kotlinx.android.synthetic.main.fragment_foodmenu_home.*

@FragmentRoute(name = "FoodMenuHome", containerId = R.id.foodMenuContainer)
class FoodMenuHomeFragment : Fragment(R.layout.fragment_foodmenu_home), FoodMenuHomeView {

    private val presenter by lazy { FoodMenuHomePresenter(navigator, this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val restoredState = savedInstanceState?.getInt(KEY_STATE)
        presenter.start(restoredState)

        launchMenuSelectionButton.setOnClickListener {
            presenter.onLaunchMenuSelectionClick()
        }

        numberOfCustomersEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                presenter.onNumberOfCustomersTextChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })
    }

    override fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_STATE, presenter.getState())
    }

    private companion object {
        private const val KEY_STATE = "KEY_STATE"
    }
}

class FoodMenuHomePresenter(private val navigator: Navigator, private val view: FoodMenuHomeView) {

    private var currentNumberOfCustomers = 0

    fun start(restoredState: Int?) {
        currentNumberOfCustomers = restoredState ?: 0
        navigator.onResult<String, FoodMenuSelection> { foodOrder ->
            view.showToast("Order completed for $currentNumberOfCustomers customers: $foodOrder")
        }
    }

    fun onLaunchMenuSelectionClick() {
        if (currentNumberOfCustomers > 0) {
            navigator.navigateForResult(FoodMenuSelection(numberOfCustomers = currentNumberOfCustomers))
        } else {
            view.showToast("you must insert the number of customers first")
        }
    }

    fun onNumberOfCustomersTextChange(newText: String) {
        if (newText.toIntOrNull() != null) {
            currentNumberOfCustomers = newText.toInt()
        }
    }

    fun getState(): Int = currentNumberOfCustomers
}

interface FoodMenuHomeView {

    fun showToast(text: String)
}