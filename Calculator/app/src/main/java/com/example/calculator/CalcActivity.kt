package com.example.calculator

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.calculator.databinding.CalculatorBinding
import kotlinx.coroutines.launch

class CalcActivity : AppCompatActivity() {
    private val viewModel: CalcViewModel by viewModels()
    private lateinit var binding: CalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.input.text = state.input
                binding.result.text = state.result
                binding.result.setTextColor(getColor(state.color))
            }
        }

        setEventListeners()
    }

    private fun setInputButtonListener(button: android.widget.Button) {
        button.setOnClickListener {
            viewModel.onInput(button.text.toString())
        }
    }

    private fun setEventListeners() {
        setInputButtonListener(binding.button1)
        setInputButtonListener(binding.button2)
        setInputButtonListener(binding.button3)
        setInputButtonListener(binding.button4)
        setInputButtonListener(binding.button5)
        setInputButtonListener(binding.button6)
        setInputButtonListener(binding.button7)
        setInputButtonListener(binding.button8)
        setInputButtonListener(binding.button9)
        setInputButtonListener(binding.button10)
        setInputButtonListener(binding.button11)
        setInputButtonListener(binding.button12)
        setInputButtonListener(binding.button13)
        setInputButtonListener(binding.button14)
        binding.button15.setOnClickListener { viewModel.onDeleteLast() }
        setInputButtonListener(binding.button16)
    }
}