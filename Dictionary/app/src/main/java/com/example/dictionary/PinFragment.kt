package com.example.dictionary

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentPinBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PinFragment : Fragment(R.layout.fragment_pin) {
    private lateinit var binding: FragmentPinBinding

    private val viewModel: PinViewModel by activityViewModels {
        PinViewModelFactory(SettingsStorage(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPinBinding.bind(view)

        viewModel.state
            .onEach { loginState ->
                updateView(loginState)

                if ((loginState.currentState == State.ENTER_PIN || loginState.currentState == State.REPEAT_PIN) && loginState.pin.length == 4 && !loginState.isError) {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.pinFragment, true)
                        .build()
                    findNavController().navigate(R.id.action_pinFragment_to_listFragment, null, navOptions)
                }

                if (loginState.isError) {
                    Toast.makeText(requireContext(), resources.getString(R.string.pin_error), Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        val buttons = listOf(
            binding.button0,
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
            binding.button5,
            binding.button6,
            binding.button7,
            binding.button8,
            binding.button9,
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.appendToPin(button.text.toString())
            }
        }
    }

    private fun updateView(loginState: LoginState) {
        when (loginState.currentState) {
            State.ENTER_PIN -> binding.title.text = resources.getString(R.string.enter_pin)
            State.CREATE_PIN -> binding.title.text = resources.getString(R.string.create_pin)
            State.REPEAT_PIN -> binding.title.text = resources.getString(R.string.repeat_pin)
        }

        val pinLength = loginState.pin.length
        binding.pinCircle1.setBackgroundResource(if (pinLength > 0) R.drawable.pin_circle_filled else R.drawable.pin_circle)
        binding.pinCircle2.setBackgroundResource(if (pinLength > 1) R.drawable.pin_circle_filled else R.drawable.pin_circle)
        binding.pinCircle3.setBackgroundResource(if (pinLength > 2) R.drawable.pin_circle_filled else R.drawable.pin_circle)
        binding.pinCircle4.setBackgroundResource(if (pinLength > 3) R.drawable.pin_circle_filled else R.drawable.pin_circle)
    }
}
