package com.example.dictionary

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class SettingsStorage(
    private val context: Context
) {
    // в application и прокидывать сюда, чтобы не создавались несколько dataStore
    private val Context.dataStore by preferencesDataStore(name = "setting")
    private val pinKey = stringPreferencesKey("pin_key")

    suspend fun savePin(pin: String) {
        context.dataStore.edit {
            it[pinKey] = pin
        }
    }

    suspend fun getPin(): String? {
        Log.i("getPin", context.dataStore.data.firstOrNull()?.get(pinKey).toString())
        return context.dataStore.data.firstOrNull()?.get(pinKey)
    }
}