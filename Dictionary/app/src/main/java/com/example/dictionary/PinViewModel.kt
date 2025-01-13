package com.example.dictionary

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

enum class State {
    CREATE_PIN,
    REPEAT_PIN,
    ENTER_PIN,
}

data class LoginState(
    val currentState: State,
    val pin: String = "",
    val isError: Boolean = false
)

class PinViewModel(private val settingStorage: SettingsStorage) : ViewModel() {
    private val _state = MutableStateFlow(LoginState(State.CREATE_PIN))
    val state = _state.asStateFlow()

    private var actualPin: String? = null

    init {
        viewModelScope.launch {
            loadPin()
        }
    }

    fun appendToPin(value: String) {
        val newPin = _state.value.pin + value
        updateState(newPin)
    }

    private fun updateState(newPin: String) {
        val currentState = _state.value.currentState

        when (currentState) {
            State.ENTER_PIN -> {
                if (newPin.length == 4) {
                    if (newPin == actualPin) {
                        _state.value = _state.value.copy(pin = newPin, isError = false)
                    } else {
                        _state.value = _state.value.copy(pin = "", isError = true)
                    }
                } else {
                    _state.value = _state.value.copy(pin = newPin, isError = false)
                }
            }
            State.CREATE_PIN -> {
                if (newPin.length == 4) {
                    actualPin = newPin
                    _state.value = _state.value.copy(pin = "", currentState = State.REPEAT_PIN, isError = false)
                } else {
                    _state.value = _state.value.copy(pin = newPin, isError = false)
                }
            }
            State.REPEAT_PIN -> {
                if (newPin.length == 4) {
                    if (newPin == actualPin) {
                        viewModelScope.launch {
                            settingStorage.savePin(newPin)
                        }
                        _state.value = _state.value.copy(isError = false)
                    } else {
                        _state.value = _state.value.copy(pin = "", currentState = State.CREATE_PIN, isError = true)
                    }
                } else {
                    _state.value = _state.value.copy(pin = newPin, isError = false)
                }
            }
        }
    }

    private suspend fun loadPin() {
        actualPin = settingStorage.getPin()
        _state.value = if (actualPin.isNullOrBlank()) {
            LoginState(State.CREATE_PIN)
        } else {
            Log.i("actualPin", actualPin.toString())
            LoginState(State.ENTER_PIN)
        }
    }
}