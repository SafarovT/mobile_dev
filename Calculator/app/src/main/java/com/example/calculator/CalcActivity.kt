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
            }
        }

        setEventListeners()
    }

    private fun setEventListeners() {
        binding.button1.setOnClickListener { viewModel.onInput("1") }
        binding.button2.setOnClickListener { viewModel.onInput("2") }
        binding.button3.setOnClickListener { viewModel.onInput("3") }
        binding.button4.setOnClickListener { viewModel.onInput("4") }
        binding.button5.setOnClickListener { viewModel.onInput("5") }
        binding.button6.setOnClickListener { viewModel.onInput("6") }
        binding.button7.setOnClickListener { viewModel.onInput("7") }
        binding.button8.setOnClickListener { viewModel.onInput("8") }
        binding.button9.setOnClickListener { viewModel.onInput("9") }
        binding.button10.setOnClickListener { viewModel.onInput("0") }
        binding.button11.setOnClickListener { viewModel.onInput("+") }
        binding.button12.setOnClickListener { viewModel.onInput("-") }
        binding.button13.setOnClickListener { viewModel.onInput("×") }
        binding.button14.setOnClickListener { viewModel.onInput("÷") }
        binding.button15.setOnClickListener { viewModel.onDeleteLast() }
        binding.button16.setOnClickListener { viewModel.onInput(".") }
    }
}