package com.example.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalcViewModel : ViewModel() {
    data class State(
        val input: String = "",
        val result: String = "",
        val color: Int = R.color.black
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val calculator = Calculator()

    fun onInput(input: String) {
        val newInput = _state.value.input + input
        updateState(newInput)
    }

    fun onDeleteLast() {
        val currentInput = _state.value.input
        if (currentInput.isNotEmpty()) {
            updateState(currentInput.dropLast(1))
        }
    }

    private fun updateState(newInput: String) {
        var newColor = R.color.black
        val newResult = try {
            calculator.calculateExpression(newInput).toString()
        } catch (e: ArithmeticException) {
            newColor = R.color.critical
            "Ошибка"
        } catch (e: Exception) {
            ""
        }

        _state.value = State(input = newInput, result = newResult, color = newColor)
    }
}