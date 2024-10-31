package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.CalculatorBinding

class CalcActivity : AppCompatActivity() {
    private val calculator = Calculator()
    private lateinit var binding: CalculatorBinding
    private var result: String = ""
        set(value) {
            field = value
            binding.result.text = value
        }
    private var input: String = ""
        set(value) {
            field = value
            binding.input.text = value
            try {
                result = calculator.calculateExpression(value).toString()
                binding.result.setTextColor(getColor(R.color.black))
            } catch(_: ArithmeticException) {
                result = getString(R.string.error)
                binding.result.setTextColor(getColor(R.color.critical))
            } catch(_: Exception) {}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setEventListeners()
    }

    private fun setInputButtonListener(button: android.widget.Button) {
        button.setOnClickListener {
            input += button.text
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
        binding.button15.setOnClickListener {
            input = input.dropLast(1)
        }
        setInputButtonListener(binding.button16)
    }
}