package com.example.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(private val storage: SettingStorage) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
		return if (viewModelClass == LoginViewModel::class.java) {
			LoginViewModel(storage) as T
		} else {
			throw IllegalArgumentException("ViewModel class not recognized")
		}
	}
}