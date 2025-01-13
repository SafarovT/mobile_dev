package com.example.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PinViewModelFactory(private val settingsStorage: SettingsStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PinViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PinViewModel(settingsStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}